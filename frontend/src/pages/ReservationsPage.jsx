import { useEffect, useState } from 'react';
import axios from 'axios';
import styles from '../css/ReservationsPage.module.css';
import CONFIG from '../config';
import {
  FaClock,
  FaBolt,
  FaCheck,
  FaTimes,
  FaMapMarkerAlt,
  FaStopwatch,
  FaBatteryFull,
  FaPlug
} from 'react-icons/fa';
import { BsInfoCircle } from 'react-icons/bs';
import Sidebar from '../components/Sidebar';
import { useUser } from '../context/UserProvider';

export default function ReservationsPage() {
  const [reservations, setReservations] = useState([]);
  const [error, setError] = useState('');
  const [me, setMe] = useState(null);
  const token = localStorage.getItem('token');
  const { userType } = useUser();
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
      setTimers((prev) => ({ ...prev, [reservationId]: 300 }));

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
    }""
  }, [token]);

 useEffect(() => {
  if (!token || !me) return;
  console.log('Fetching reservations for:', me);

  const url = me.type === 'administrators'
    ? `${CONFIG.API_URL}v1/reservation`
    : `${CONFIG.API_URL}v1/reservation/user/${me.id}`;

  axios
    .get(url, {
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
    <div className={styles.page} id="reservations-page">
      <Sidebar />
      <div className={styles.container}>
        { userType === 'administrators' && (
          <h1 className={styles.title} id="admin-reservations-title">All Reservations</h1>
        )}
        { userType === 'driver' && (
          <h1 className={styles.title} id="user-reservations-title">My Reservations</h1>
        )}
        {reservations.length === 0 ? (
          <p className={styles.message} id="no-reservations">You have no reservations.</p>
        ) : (
          <div className={styles.grid} id="reservations-list">
            {reservations.map((reservation) => {
              const cp = reservation.chargingPoint;
              const cs = cp?.chargingStation;

              return (
                <div
                  data-charging-point-id={cp?.id}
                  key={reservation.id}
                  className={`${styles.card} ${
                    reservation.status === 'TO_BE_USED'
                      ? styles.TO_BE_USED
                      : reservation.status === 'USED'
                      ? styles.confirmed
                      : reservation.status === 'CANCELLED'
                      ? styles.cancelled
                      : ''
                  }`}
                  id={`reservation-card-${reservation.id}`}
                >
                  {/* Header */}
                  <div className={styles.cardHeader}>
                    <span className={styles.headerIcon}>⚡</span>
                    <h3 className={styles.cardTitle} id={`reservation-brand-${reservation.id}`}>
                      <FaPlug className={styles.inlineIcon} /> {cp?.brand || 'Charging Point'}
                    </h3>
                  </div>

                  {/* Details */}
                  <div className={styles.cardBody}>
                    <p id={`reservation-id-${reservation.id}`}>
                      <strong>Charging Point ID:</strong> {reservation.chargingPoint.id}
                    </p>
                    <p id={`reservation-status-${reservation.id}`}>
                      <BsInfoCircle className={styles.inlineIcon} />
                      <strong>Status:</strong> {reservation.status}
                    </p>

                    <p id={`reservation-start-${reservation.id}`}>
                      <FaClock className={styles.inlineIcon} />
                      <strong>Start:</strong> {new Date(reservation.startTime).toLocaleString('en-GB', { timeZone: 'Europe/Lisbon' })}
                    </p>

                    <p id={`reservation-end-${reservation.id}`}>
                      <FaClock className={styles.inlineIcon} />
                      <strong>End:</strong> {new Date(reservation.endTime).toLocaleString('en-GB', { timeZone: 'Europe/Lisbon' })}
                    </p>

                    <p>
                      <FaBolt className={styles.inlineIcon} />
                      <strong>Price per kWh:</strong> €{cp?.pricePerKWh?.toFixed(2) ?? 'N/A'}
                    </p>

                    <p>
                      <FaStopwatch className={styles.inlineIcon} />
                      <strong>kWh per minute:</strong> {cp?.chargingRateKWhPerMinute?.toFixed(2) ?? 'N/A'}
                    </p>


                    {cs && (
                      <p>
                        <FaMapMarkerAlt className={styles.inlineIcon} />
                        <strong>Location:</strong> {cs.address}, {cs.cityName}
                      </p>
                    )}
                    {reservation.status === 'TO_BE_USED' && (
                      <>
                        <button
                          className={styles.confirmButton}
                          id={`generate-otp-button-${reservation.id}`}
                          onClick={() => handleGenerateOtp(reservation.id)}
                        >
                          Generate Start Code
                        </button>
                        {otpData[reservation.id] && (
                          <div className={styles.otpCard}>
                            <p id={`otp-code-${reservation.id}`} className={styles.otpCode}>
                              <strong>OTP:</strong> {otpData[reservation.id].code}
                            </p>
                            <p className={styles.expiry}>
                              <strong>Expires in:</strong> {timers[reservation.id]}s
                            </p>
                          </div>
                        )}
                      </>
                    )}
                  </div>
                </div>

              );
            })}
          </div>
        )}
      </div>
    </div>
  );
}

