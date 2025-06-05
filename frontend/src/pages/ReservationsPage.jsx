import { useEffect, useState } from 'react';
import axios from 'axios';
import styles from '../css/ReservationsPage.module.css';
import CONFIG from '../config';
import Sidebar from '../components/Sidebar';

export default function ReservationsPage() {
  const [reservations, setReservations] = useState([]);
  const [error, setError] = useState('');
  const [me, setMe] = useState(null); 
  const token = localStorage.getItem('token');

  const [otpData, setOtpData] = useState({});
  const [timers, setTimers] = useState({});

const handleGenerateOtp = async (reservationId) => {
  try {
    const res = await axios.post(
      `${CONFIG.API_URL}v1/reservation/${reservationId}/otp`,
      {},
      { headers: { Authorization: `Bearer ${token}` } }
    );
    const code = res.data;

    setOtpData((prev) => ({ ...prev, [reservationId]: code }));
    setTimers((prev) => ({ ...prev, [reservationId]: 60 }));

    const intervalId = setInterval(() => {
      setTimers((prev) => {
        const newTime = (prev[reservationId] || 0) - 1;
        if (newTime <= 0) {
          clearInterval(intervalId);
          setOtpData((prevOtp) => ({ ...prevOtp, [reservationId]: null }));
          return { ...prev, [reservationId]: 0 };
        }
        return { ...prev, [reservationId]: newTime };
      });
    }, 1000);
  } catch (err) {
    console.error('Failed to generate OTP:', err);
  }
};


  useEffect(() => {
    const localMe = localStorage.getItem('me');
    if (localMe) {
      setMe(JSON.parse(localMe));
    } else {
      axios
        .get(`${CONFIG.API_URL}auth/me`, {
          headers: { Authorization: `Bearer ${token}` },
        })
        .then((res) => {
          setMe(res.data);
          localStorage.setItem('me', JSON.stringify(res.data));
        })
        .catch((err) => {
          console.error('Failed to fetch user info:', err);
          setError('Unable to fetch user information.');
        });
    }
  }, [token]);

    useEffect(() => {
    if (!token || !me) return;

    axios
      .get(`${CONFIG.API_URL}v1/reservation/user/${me.id}`, {
        headers: { Authorization: `Bearer ${token}` },
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
<div className={styles.page} data-testid="reservations-page">
  <Sidebar />
  <div className={styles.container}>
    <h2 className={styles.title} data-testid="reservations-title">My Reservations</h2>

    {reservations.length === 0 ? (
      <p className={styles.message} data-testid="no-reservations">You have no reservations.</p>
    ) : (
      <div className={styles.grid} data-testid="reservations-list">
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
            data-testid={`reservation-card-${reservation.id}`}
          >
            <h3 className={styles.cardTitle}>
              {cp?.brand || 'Charging Point'}
            </h3>

            <p><strong>Status:</strong> {reservation.status}</p>
            <p><strong>Start:</strong> {new Date(reservation.startTime).toLocaleString()}</p>
            <p><strong>End:</strong> {new Date(reservation.endTime).toLocaleString()}</p>

            <p><strong>Price per kWh:</strong> €{cp?.pricePerKWh?.toFixed(2) ?? 'N/A'}</p>
            <p><strong>Price per Minute:</strong> €{cp?.pricePerMinute?.toFixed(2) ?? 'N/A'}</p>
            <p><strong>Available:</strong> {cp?.available ? 'Yes' : 'No'}</p>

            {cs && (
              <p><strong>Location:</strong> {cs.address}, {cs.cityName}</p>
            )}

            {reservation.status === 'PENDING' && (
              <div style={{ marginTop: '10px' }}>
                <button
                  className={styles.confirmButton}
                  onClick={() => handleGenerateOtp(reservation.id)}
                >
                  Generate Start Code
                </button>

                {otpData[reservation.id] && (
                  <div className={styles.message} style={{ marginTop: '0.5rem' }}>
                    <p><strong>OTP Code:</strong> {otpData[reservation.id]?.code}</p>
                    <p><strong>Expires in:</strong> {timers[reservation.id]}s</p>
                  </div>
                )}
              </div>
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
