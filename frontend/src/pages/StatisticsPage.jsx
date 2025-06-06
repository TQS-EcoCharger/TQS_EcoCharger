import { useEffect, useState } from 'react';
import {
  PieChart, Pie, Cell, Tooltip, ResponsiveContainer,
  BarChart, Bar, XAxis, YAxis, Legend, CartesianGrid
} from 'recharts';
import axios from 'axios';
import CONFIG from '../config';
import styles from '../css/StatisticsPage.module.css';
import Sidebar from '../components/Sidebar';

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff8042', '#8dd1e1'];

function InfoCard({ title, value }) {
  return (
    <div className={styles.card}>
      <h3 className={styles.cardTitle}>{title}</h3>
      <p className={styles.cardValue}>{value}</p>
    </div>
  );
}

// Utilitário para agregações
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
  const token = localStorage.getItem('token');

  useEffect(() => {
    axios.get(`${CONFIG.API_URL}v1/sessions`, {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(res => {
        const allSessions = res.data;

        const formatted = allSessions.map(session => ({
          id: session.id,
          date: session.startTime,
          kWh: session.energyDelivered || 0,
          cost: session.totalCost || 0,
          stationName: session.chargingPoint?.name || 'N/A',
          region: session.chargingPoint?.location?.region || 'Unknown',
          city: session.chargingPoint?.location?.city || 'Unknown',
          month: new Date(session.startTime).toLocaleString('default', { month: 'short' })
        }));

        setPastSessions(formatted);

        setMonthlyData(aggregateBy(formatted, 'month', 'kWh'));
        setRegionData(aggregateBy(formatted, 'region', 'id', true));
        setCo2Data(aggregateBy(formatted, 'city', 'kWh', false, val => val * 0.2));

        const avg = (
          formatted.reduce((acc, s) => acc + s.cost, 0) / formatted.length
        ).toFixed(2);

        setAverageCost(avg);
        const peak = [...aggregateBy(formatted, 'month', 'kWh')].sort((a, b) => b.kWh - a.kWh)[0]?.month || '';
        setPeakMonth(peak);
      })
      .catch(err => console.error('Failed to load sessions:', err));
  }, [token]);



  return (
    <div className={styles.container}>
      <div className={styles.sidebarPanel}>
        <Sidebar />
      </div>

      <div className={styles.mainContent}>
        <aside className={styles.sessionList}>
          <h2 className={styles.sectionTitle}>Past Charging Sessions</h2>
          {pastSessions.map(session => (
            <div key={session.id} className={styles.card}>
              <p><strong>Date:</strong> {new Date(session.date).toLocaleDateString()}</p>
              <p><strong>Consumption:</strong> {session.kWh} kWh</p>
              <p><strong>Cost:</strong> €{session.cost.toFixed(2)}</p>
              <p><strong>Station:</strong> {session.stationName}</p>
            </div>
          ))}
        </aside>

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
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          </div>

          <div className={styles.chartGrid}>
            <InfoCard title="Average Cost per Session" value={`€${averageCost}`} />
            <InfoCard title="Peak Usage Month" value={peakMonth} />

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
                  <Tooltip />
                  <Legend />
                  <Bar dataKey="kWh" fill="#82ca9d" name="co2Kg" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}

export default StatisticsPage;
