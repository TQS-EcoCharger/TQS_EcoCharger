import { useEffect, useState } from 'react';
import axios from 'axios';
import styles from '../css/ReservationsPage.module.css';
import CONFIG from '../config';
import Sidebar from '../components/Sidebar';

export default function ReservationsPage() {
  const [reservations, setReservations] = useState([]);
  const [error, setError] = useState('');
  const token = localStorage.getItem('token');
  const me = JSON.parse(localStorage.getItem('me'));

  useEffect(() => {
    if (!token || !me) {
      setError('You must be logged in to view your reservations.');
      return;
    }

    axios
      .get(`${CONFIG.API_URL}v1/reservation/user/${me.id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => setReservations(res.data))
      .catch((err) => {
        console.error('Failed to load reservations:', err);
        setError('Failed to load reservations.');
      });
  }, [token, me]);

  if (error) {
    return <div className={styles.message}>{error}</div>;
  }

  return (
    <div className={styles.page}>
      <Sidebar />
    <div className={styles.container}>
      <h2 className={styles.title}>My Reservations</h2>
      {reservations.length === 0 ? (
        <p className={styles.message}>You have no reservations.</p>
      ) : (
        <div className={styles.grid}>
          {reservations.map((reservation) => {
            const cp = reservation.chargingPoint;
            const cs = cp?.chargingStation;

            return (
                <div
                  key={reservation.id}
                  className={`${styles.card} ${
                    reservation.status === 'PENDING'
                      ? styles.pending
                      : reservation.status === 'CONFIRMED'
                      ? styles.confirmed
                      : reservation.status === 'CANCELLED'
                      ? styles.cancelled
                      : ''
                  }`}
                >
                <h3 className={styles.cardTitle}>{cp?.brand || 'Charging Point'}</h3>

                <p><strong>Status:</strong> {reservation.status}</p>
                <p><strong>Start:</strong> {new Date(reservation.startTime).toLocaleString()}</p>
                <p><strong>End:</strong> {new Date(reservation.endTime).toLocaleString()}</p>

                <p><strong>Price per kWh:</strong> €{cp?.pricePerKWh?.toFixed(2) ?? 'N/A'}</p>
                <p><strong>Price per Minute:</strong> €{cp?.pricePerMinute?.toFixed(2) ?? 'N/A'}</p>
                <p><strong>Available:</strong> {cp?.available ? 'Yes' : 'No'}</p>

                {cs && (
                  <p><strong>Location:</strong> {cs.address}, {cs.cityName}</p>
                )}

                {cp?.connectors?.length > 0 && (
                  <>
                    <p><strong>Connectors:</strong></p>
                    <ul className={styles.connectorList}>
                      {cp.connectors.map((conn) => (
                        <li key={conn.id} className={styles.connectorItem}>
                          <span><strong>Type:</strong> {conn.connectorType}</span>,
                          <span> <strong>Power:</strong> {conn.ratedPowerKW} kW</span>,
                          <span> <strong>Voltage:</strong> {conn.voltageV} V</span>,
                          <span> <strong>Current:</strong> {conn.currentA} A</span>
                        </li>
                      ))}
                    </ul>
                  </>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
    </div>
  );
}
