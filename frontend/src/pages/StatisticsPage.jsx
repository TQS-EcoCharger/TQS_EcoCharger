import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import { FaCalendarAlt, FaBolt, FaEuroSign, FaMapMarkerAlt, FaCarSide, FaBatteryHalf, FaGasPump } from 'react-icons/fa';
import {FaBurn, FaFireAlt } from 'react-icons/fa';

import { MdOutlineCarCrash } from 'react-icons/md';
import { BsCheckCircle, BsXCircle } from 'react-icons/bs';
import {
  PieChart, Pie, Cell, Tooltip, ResponsiveContainer,
  BarChart, Bar, XAxis, YAxis, Legend, CartesianGrid
} from 'recharts';
import axios from 'axios';
import CONFIG from '../config';
import styles from '../css/StatisticsPage.module.css';
import Sidebar from '../components/Sidebar';

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff8042', '#8dd1e1'];

const fuelTypes = {
  gasolina: { price: 1.85, efficiency: 6.5 },
  gasoleo: { price: 1.70, efficiency: 5.5 },
  gpl: { price: 1.00, efficiency: 8.0 }
};


function calculateSavingsMultiCity({ energyDelivered, evCost, city, evEfficiency = 0.15, cityPrices, manualFuelPrices }) {
  const km = energyDelivered / evEfficiency;
  const result = {};

  const prices = cityPrices[city] || {};

  for (const type of ['gasolina', 'gasoleo', 'gpl']) {
    const price = prices[type] || manualFuelPrices?.[type] || fuelTypes[type].price;
    const efficiency = fuelTypes[type].efficiency;
    const litres = (km * efficiency) / 100;
    const combustionCost = litres * price;
    result[type] = {
      combustionCost: combustionCost.toFixed(2),
      savings: (combustionCost - evCost).toFixed(2)
    };
  }

  return { km: km.toFixed(1), ...result };
}



function InfoCard({ title, value }) {
  return (
    <div className={styles.card}>
      <h3 className={styles.cardTitle}>{title}</h3>
      <p className={styles.cardValue}>{value}</p>
    </div>
  );
}

function aggregateBy(data, groupKey, valueKey, countMode = false, transformFn = val => val) {
  const result = {};

  data.forEach(item => {
    const key = item[groupKey] || 'Unknown';
    const value = countMode ? 1 : (item[valueKey] || 0);
    const finalValue = transformFn(value);

    if (!result[key]) result[key] = 0;
    result[key] += finalValue;
  });

  return Object.entries(result).map(([key, value]) => ({
    [groupKey]: key,
    [countMode ? 'sessions' : valueKey]: parseFloat(value.toFixed(2))
  }));
}

function StatisticsPage() {
  const [pastSessions, setPastSessions] = useState([]);
  const [monthlyData, setMonthlyData] = useState([]);
  const [regionData, setRegionData] = useState([]);
  const [co2Data, setCo2Data] = useState([]);
  const [averageCost, setAverageCost] = useState('');
  const [peakMonth, setPeakMonth] = useState('');
  const [totalSavings, setTotalSavings] = useState({ gasolina: '0.00', gasoleo: '0.00', gpl: '0.00' });
  const token = localStorage.getItem('token');
  const [cityFuelPrices] = useState({});
  const [manualFuelPrices, setManualFuelPrices] = useState({
    gasolina: fuelTypes.gasolina.price,
    gasoleo: fuelTypes.gasoleo.price,
    gpl: fuelTypes.gpl.price
  });


  useEffect(() => {
    axios.get(`${CONFIG.API_URL}v1/sessions`, {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(res => {
        const allSessions = res.data;
        const formatted = allSessions.map(session => ({
          id: session.id,
          user: session.user?.name,
          carModel: session.car?.name,
          carLicense: session.car?.licensePlate,
          date: session.startTime,
          endDate: session.endTime,
          energyDelivered: session.energyDelivered || 0,
          kWh: session.energyDelivered || 0,
          cost: session.totalCost || 0,
          status: session.status,
          battery: session.initialBatteryLevel || 0,
          stationCityName: session.chargingPoint?.chargingStation?.cityName || 'N/A',
          stationAddress: session.chargingPoint?.chargingStation?.address || 'N/A',
          city: session.chargingPoint?.chargingStation?.cityName || 'N/A',
          region: session.chargingPoint?.chargingStation?.cityName || 'N/A',
          stationName: session.chargingPoint?.chargingStation?.name || 'N/A',
          month: new Date(session.startTime).toLocaleString('default', { month: 'short' })
        }));

        setPastSessions(formatted);
        setMonthlyData(aggregateBy(formatted, 'month', 'kWh'));
        setRegionData(aggregateBy(formatted, 'region', 'id', true));
        setCo2Data(
          aggregateBy(formatted, 'city', 'kWh', false, val => val * 0.2).map(entry => ({
            ...entry,
            co2Kg: entry.kWh,
            kWh: undefined
          }))
        );

        const avg = (
          formatted.reduce((acc, s) => acc + s.cost, 0) / formatted.length
        ).toFixed(2);
        setAverageCost(avg);

        const peak = [...aggregateBy(formatted, 'month', 'kWh')].sort((a, b) => b.kWh - a.kWh)[0]?.month || '';
        setPeakMonth(peak);

        const totals = { gasolina: 0, gasoleo: 0, gpl: 0 };
        formatted.forEach(s => {
        const savings = calculateSavingsMultiCity({
          energyDelivered: s.energyDelivered,
          evCost: s.cost,
          city: s.city,
          cityPrices: cityFuelPrices
        });
          Object.keys(totals).forEach(type => {
            totals[type] += parseFloat(savings[type].savings);
          });
        });

        setTotalSavings({
          gasolina: totals.gasolina.toFixed(2),
          gasoleo: totals.gasoleo.toFixed(2),
          gpl: totals.gpl.toFixed(2)
        });
      })
      .catch(err => console.error('Failed to load sessions:', err));
  }, [token]);

  useEffect(() => {
  const totals = { gasolina: 0, gasoleo: 0, gpl: 0 };
  pastSessions.forEach(s => {
    const savings = calculateSavingsMultiCity({
      energyDelivered: s.energyDelivered,
      evCost: s.cost,
      city: s.city,
      cityPrices: cityFuelPrices,
      manualFuelPrices
    });
    Object.keys(totals).forEach(type => {
      totals[type] += parseFloat(savings[type].savings);
    });
  });

  setTotalSavings({
    gasolina: totals.gasolina.toFixed(2),
    gasoleo: totals.gasoleo.toFixed(2),
    gpl: totals.gpl.toFixed(2)
  });
}, [manualFuelPrices, pastSessions, cityFuelPrices]);


  return (
    <div className={styles.container}>
      <div className={styles.sidebarPanel}>
        <Sidebar />
      </div>

      <div className={styles.mainContent}>
        <section className={styles.chartGrid}>
          <InfoCard title="Average Cost per Session" value={`€${averageCost}`} />
          <InfoCard title="Peak Usage Month" value={peakMonth} />
          <InfoCard title="Savings vs Gasoline" value={`€${totalSavings.gasolina}`} />
          <InfoCard title="Savings vs Diesel" value={`€${totalSavings.gasoleo}`} />
          <InfoCard title="Savings vs GPL" value={`€${totalSavings.gpl}`} />
          <div className={styles.cardStatistics}>
            <h3 className={styles.cardTitleStatistics}>Editar Preços de Combustível (€)</h3>

            <div className={styles.priceListStatistics}>
              {['gasolina', 'gasoleo', 'gpl'].map((type) => (
                <div key={type} className={styles.inputGroup}>
                  <label className={styles.cardLabel}>{type.toUpperCase()}:</label>
                  <input
                    type="number"
                    step="0.01"
                    min="0"
                    value={manualFuelPrices[type]}
                    onChange={(e) =>
                      setManualFuelPrices(prev => ({
                        ...prev,
                        [type]: parseFloat(e.target.value) || 0
                      }))
                    }
                    className={styles.input}
                  />
                </div>
              ))}
            </div>
            <div className={styles.priceSummary}>
              <div className={styles.priceRow}>
                <FaGasPump className={styles.icon} />
                <span>Gasolina: €{manualFuelPrices.gasolina.toFixed(2)}</span>
              </div>
              <div className={styles.priceRow}>
                <FaBurn className={styles.icon} />
                <span>Gasóleo: €{manualFuelPrices.gasoleo.toFixed(2)}</span>
              </div>
              <div className={styles.priceRow}>
                <FaFireAlt className={styles.icon} />
                <span>GPL: €{manualFuelPrices.gpl.toFixed(2)}</span>
              </div>
            </div>
          </div>

        </section>

        <section className={styles.chartArea}>
          <div className={styles.chartBox}>
            <h2 className={styles.sectionTitle}>Monthly Consumption</h2>
            <ResponsiveContainer width="100%" height={250}>
              <PieChart>
                <Pie data={monthlyData} dataKey="kWh" nameKey="month" outerRadius={80} label>
                  {monthlyData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip formatter={(value) => `${value} kWh`} />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className={styles.chartBox}>
            <h3 className={styles.sectionTitle}>Sessions by Region</h3>
            <ResponsiveContainer width="100%" height={200}>
              <BarChart data={regionData}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="region" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="sessions" fill="#8884d8" />
              </BarChart>
            </ResponsiveContainer>
          </div>

          <div className={styles.chartBox}>
            <h3 className={styles.sectionTitle}>CO2 Saved per City</h3>
            <ResponsiveContainer width="100%" height={200}>
              <BarChart data={co2Data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="city" />
                <YAxis />
                <Tooltip formatter={(value) => `${value} kg`} />
                <Legend />
                <Bar dataKey="co2Kg" fill="#82ca9d" name="CO₂ Saved (kg)" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </section>

        <aside className={styles.sessionList}>
          <h2 className={styles.sectionTitle}>Past Charging Sessions</h2>
          {pastSessions.map(session => (
            <motion.div
              key={session.id}
              className={styles.sessionCard}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ duration: 0.3, delay: session.id * 0.05 }}
            >
              <div className={styles.sessionHeader}>
                <h4 className={styles.sessionTitle}><FaCarSide className={styles.icon} /> {session.carModel}</h4>
                <span className={`${styles.status} ${session.status === 'COMPLETED' ? styles.completed : styles.pending}`}>
                  {session.status === 'COMPLETED' ? <BsCheckCircle /> : <BsXCircle />} {session.status}
                </span>
              </div>
              <div className={styles.sessionDetail}><FaCalendarAlt className={styles.icon} /> <strong>Date:</strong> {new Date(session.date).toLocaleDateString()}</div>
              <div className={styles.sessionDetail}><FaBolt className={styles.icon} /> <strong>Energy:</strong> {session.energyDelivered} kWh</div>
              <div className={styles.sessionDetail}><FaEuroSign className={styles.icon} /> <strong>Cost:</strong> €{session.cost.toFixed(2)}</div>
              <div className={styles.batteryContainer}>
                <FaBatteryHalf className={styles.icon} />
                <strong>Battery:</strong>
                <div className={styles.batteryBar}>
                  <motion.div
                    className={styles.batteryLevel}
                    initial={{ width: 0 }}
                    animate={{ width: `${session.battery}%` }}
                    transition={{ duration: 1.5 }}
                  />
                  <span className={styles.batteryText}>{session.battery}%</span>
                </div>
              </div>
              <div className={styles.sessionDetail}><MdOutlineCarCrash className={styles.icon} /> <strong>License:</strong> {session.carLicense}</div>
              <div className={styles.sessionDetail}><FaMapMarkerAlt className={styles.icon} /> <strong>Location:</strong> {session.stationAddress}, {session.stationCityName}</div>
            </motion.div>
          ))}
        </aside>
      </div>
    </div>
  );
}

export default StatisticsPage;