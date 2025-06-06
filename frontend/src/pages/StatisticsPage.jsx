/*
import { useEffect, useState } from 'react';
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, BarChart, Bar, XAxis, YAxis, Legend, CartesianGrid } from 'recharts';
import axios from 'axios';
import CONFIG from '../config';

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff8042', '#8dd1e1'];

function InfoCard({ title, value }) {
  return (
    <div className="bg-white rounded-2xl shadow p-4 text-center">
      <h3 className="text-sm font-medium text-gray-500">{title}</h3>
      <p className="text-2xl font-bold text-gray-800">{value}</p>
    </div>
  );
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
    // Mock data or fetch from backend endpoints
    axios.get(`${CONFIG.API_URL}v1/statistics`, {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then(res => {
      const { sessions, monthlyConsumption, sessionsByRegion, co2ByCity, avgCost, peak } = res.data;
      setPastSessions(sessions);
      setMonthlyData(monthlyConsumption);
      setRegionData(sessionsByRegion);
      setCo2Data(co2ByCity);
      setAverageCost(avgCost);
      setPeakMonth(peak);
    })
    .catch(err => console.error('Failed to load statistics:', err));
  }, [token]);

  return (
    <div className="flex p-6 bg-gray-100 min-h-screen">
      <aside className="w-1/3 space-y-4">
        <h2 className="text-xl font-bold mb-4">Past Charging Sessions</h2>
        {pastSessions.map(session => (
          <div key={session.id} className="bg-white rounded-2xl shadow p-4">
            <p><strong>Date:</strong> {new Date(session.date).toLocaleDateString()}</p>
            <p><strong>Consumption:</strong> {session.kWh} kWh</p>
            <p><strong>Cost:</strong> €{session.cost.toFixed(2)}</p>
            <p><strong>Station:</strong> {session.stationName}</p>
          </div>
        ))}
      </aside>

      <section className="w-2/3 pl-6 space-y-6">
        <div className="bg-white rounded-2xl shadow p-4">
          <h2 className="text-xl font-semibold mb-2">Monthly Consumption</h2>
          <ResponsiveContainer width="100%" height={250}>
            <PieChart>
              <Pie
                data={monthlyData}
                dataKey="kWh"
                nameKey="month"
                outerRadius={80}
                label
              >
                {monthlyData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </div>

        <div className="grid grid-cols-2 gap-4">
          <InfoCard title="Average Cost per Session" value={`€${averageCost}`} />
          <InfoCard title="Peak Usage Month" value={peakMonth} />

          <div className="col-span-1 bg-white rounded-2xl shadow p-4">
            <h3 className="text-lg font-semibold mb-2">Sessions by Region</h3>
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

          <div className="col-span-1 bg-white rounded-2xl shadow p-4">
            <h3 className="text-lg font-semibold mb-2">CO2 Saved per City</h3>
            <ResponsiveContainer width="100%" height={200}>
              <BarChart data={co2Data}>
                <CartesianGrid strokeDasharray="3 3" />
                <XAxis dataKey="city" />
                <YAxis />
                <Tooltip />
                <Legend />
                <Bar dataKey="co2Kg" fill="#82ca9d" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        </div>
      </section>
    </div>
  );
}

export default StatisticsPage;
*/