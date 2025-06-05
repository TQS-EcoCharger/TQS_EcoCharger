import { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import CONFIG from '../config';
import styles from '../css/SlotPage.module.css';

import { FaCar, FaClock, FaBolt, FaPowerOff, FaUser, FaKey } from 'react-icons/fa';
import { BsBatteryCharging } from 'react-icons/bs';

export default function SlotPage() {
  const { id: chargingPointId } = useParams();
  const [session, setSession] = useState(null);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(true);
  const [otp, setOtp] = useState(Array(6).fill(''));
  const [carId, setCarId] = useState('');
  const [cars, setCars] = useState([]);
  const [isOtpValid, setIsOtpValid] = useState(false);
  const token = localStorage.getItem('token');
  const navigate = useNavigate();
  const inputRefs = useRef([]);

  const meId = localStorage.getItem("me") ? JSON.parse(localStorage.getItem("me")).id : null;

  const fetchSession = async () => {
    try {
      const res = await axios.get(`${CONFIG.API_URL}v1/points/${chargingPointId}/active-session`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setSession(res.data); // ActiveSessionDTO
    } catch (err) {
      setSession(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSession();
    const interval = setInterval(fetchSession, 60000); // refresh every 60s
    return () => clearInterval(interval);
  }, [chargingPointId]);

  const handleEndCharging = async () => {
    try {
      await axios.post(`${CONFIG.API_URL}v1/sessions/${session.sessionId}/end`, {}, {
        headers: { Authorization: `Bearer ${token}` }
      });
      setMessage('Charging session ended.');
      setSession(null);
      setTimeout(() => navigate('/home'), 2000);
    } catch (err) {
      console.error('Failed to end session:', err);
      setMessage('Failed to end session.');
    }
  };

  const handleValidateOtp = async () => {
    const otpCode = otp.join('');
    if (otpCode.length < 6) {
      setMessage('Please enter a valid 6-digit OTP.');
      return;
    }

    try {
      const res = await axios.post(`${CONFIG.API_URL}v1/sessions/validate-otp`, {
        otp: otpCode,
        chargingPointId: parseInt(chargingPointId)
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });

      if (res.data.valid) {
        setIsOtpValid(true);
        const carRes = await axios.get(`${CONFIG.API_URL}v1/driver/${meId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        setCars(carRes.data.cars || []);
        setMessage('');
      } else {
        setMessage(res.data.message || 'Invalid or expired OTP.');
      }
    } catch (err) {
      console.error('OTP validation failed:', err);
      setMessage(err.response?.data?.message || 'OTP validation failed.');
    }
  };

  const handleStartCharging = async () => {
    const otpCode = otp.join('');
    if (!isOtpValid || !carId) {
      setMessage('Please validate OTP and select a vehicle.');
      return;
    }

    try {
      const res = await axios.post(`${CONFIG.API_URL}v1/sessions`, {
        otp: otpCode,
        carId: parseInt(carId),
        chargingPointId: parseInt(chargingPointId)
      }, {
        headers: { Authorization: `Bearer ${token}` }
      });

      setSession(res.data);
      setMessage('');
      setOtp(Array(6).fill(''));
      setCarId('');
      setCars([]);
      setIsOtpValid(false);
    } catch (err) {
      console.error('Failed to start session:', err);
      setMessage(err.response?.data || 'Invalid OTP or Car ID');
    }
  };

  const handleOtpChange = (value, index) => {
    if (!/^\d?$/.test(value)) return;
    const newOtp = [...otp];
    newOtp[index] = value;
    setOtp(newOtp);
    if (value && index < 5) inputRefs.current[index + 1]?.focus();
  };

  const handleKeyDown = (e, index) => {
    if (e.key === 'Backspace' && !otp[index] && index > 0) {
      inputRefs.current[index - 1]?.focus();
    }
  };

  return (
    <div className={styles.page}>
      <div className={styles.wrapper}>
        <div className={styles.stationDetails1} style={{ width: '50%', margin: '5% auto' }}>
          <h2>Charging Slot #{chargingPointId}</h2>

          {loading ? (
            <p>Loading...</p>
          ) : session ? (
            <div>
              <p><FaUser /> <strong>User:</strong> You</p>
              <p><FaCar /> <strong>Car:</strong> {session.carName}</p>
              <p><BsBatteryCharging /> <strong>Battery now:</strong> {session.currentBatteryLevel.toFixed(2)} kWh</p>
              <p><FaBolt /> <strong>Energy delivered:</strong> {session.energyDelivered.toFixed(2)} kWh</p>
              <p><FaClock /> <strong>Duration:</strong> {session.durationMinutes} min</p>
              <p><FaBolt /> <strong>Total cost:</strong> €{session.totalCost.toFixed(2)}</p>

              <div className={styles.modalButtons} style={{ marginTop: '2rem' }}>
                <button onClick={handleEndCharging} className={styles.confirmButton}>
                  <FaPowerOff /> End Charging
                </button>
              </div>
            </div>
          ) : (
            <div>
              <p>No active session.</p>
              <p><FaKey /> OTP Code:</p>
              <div className={styles.otpContainer}>
                {otp.map((digit, i) => (
                  <input
                    key={i}
                    ref={(el) => (inputRefs.current[i] = el)}
                    type="text"
                    inputMode="numeric"
                    maxLength={1}
                    className={styles.otpBox}
                    value={digit}
                    onChange={(e) => handleOtpChange(e.target.value, i)}
                    onKeyDown={(e) => handleKeyDown(e, i)}
                  />
                ))}
              </div>

              {!isOtpValid ? (
                <button onClick={handleValidateOtp} className={styles.confirmButton}>
                  Validate OTP
                </button>
              ) : (
                <>
                  <label htmlFor="carId"><FaCar /> Select Vehicle:</label>
                  <select
                    id="carId"
                    value={carId}
                    onChange={(e) => setCarId(e.target.value)}
                    className={styles.datePicker}
                  >
                    <option value="">-- Choose your vehicle --</option>
                    {cars.map((car) => (
                      <option key={car.id} value={car.id}>
                        {car.make} {car.model} (ID: {car.id})
                      </option>
                    ))}
                  </select>
                  <button onClick={handleStartCharging} className={styles.confirmButton} style={{ marginTop: '1rem' }}>
                    Start Charging
                  </button>
                </>
              )}

              {message && <p className={styles.message}>{message}</p>}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
