import { useEffect, useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import CONFIG from '../config';
import styles from '../css/SlotPage.module.css';

import { FaCar, FaClock, FaBolt, FaPowerOff, FaUser, FaKey } from 'react-icons/fa';
import { BsBatteryCharging } from 'react-icons/bs';
import Sidebar from '../components/Sidebar';

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
      setSession(res.data);
    } catch (err) {
      setSession(null);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSession();
    const interval = setInterval(fetchSession, 60000);
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
      <Sidebar />
      <div className={styles.wrapper}>
        <div className={styles.stationDetails1} style={{ width: '50%', margin: '5% auto' }}>

          {loading ? (
            <p id="loading">Loading...</p>
          ) : session ? (
            <div className={styles.sessionCard} id="session-info">
              <h2>Charging Slot #{chargingPointId}</h2>
              <p><FaCar className={styles.sessionIcon} /> <strong>Car:</strong> {session.carName || session.car.model}</p>
              <p><BsBatteryCharging className={styles.sessionIcon} /> <strong>Battery:</strong> {(session.batteryPercentage ?? 0).toFixed(2)}%</p>
              <p><FaBolt className={styles.sessionIcon} /> <strong>Energy:</strong> {(session.energyDelivered ?? 0).toFixed(2)} kWh</p>
              <p><FaClock className={styles.sessionIcon} /> <strong>Duration:</strong> {session.durationMinutes ?? 0} min</p>
              <p><FaBolt className={styles.sessionIcon} /> <strong>Cost:</strong> €{(session.totalCost ?? 0).toFixed(2)}</p>

              <button
                onClick={handleEndCharging}
                className={styles.endChargingBtn}
                id="end-charging-button"
              >
                <FaPowerOff /> End Charging
              </button>
            </div>

          ) : (
            <div id="no-session-info">
              <p>No active session.</p>
              <p><FaKey /> OTP Code:</p>
              <div className={styles.otpContainer} id="otp-container">
                {otp.map((digit, i) => (
                  <input
                    key={i}
                    ref={(el) => (inputRefs.current[i] = el)}
                    type="text"
                    inputMode="numeric"
                    maxLength={1}
                    className={styles.otpBox}
                    value={digit}
                    id={`otp-digit-${i}`}
                    onChange={(e) => handleOtpChange(e.target.value, i)}
                    onKeyDown={(e) => handleKeyDown(e, i)}
                  />
                ))}
              </div>

              {!isOtpValid ? (
                <button
                  onClick={handleValidateOtp}
                  className={styles.confirmButton}
                  id="validate-otp-button"
                >
                  Validate OTP
                </button>
              ) : (
                <>
                  <label htmlFor="car-select" style={{ marginBottom: '0.5rem', display: 'block', color: 'white' }}>
                    <FaCar /> Select Vehicle:
                  </label>
                  <Select
                    id="car-select"
                    inputId="car-select"
                    value={cars.find(c => c.id.toString() === carId)}
                    onChange={(selected) => setCarId(selected?.id.toString())}
                    options={cars.map(car => ({
                      value: car.id,
                      label: `${car.make} ${car.model} (ID: ${car.id})`,
                      id: car.id
                    }))}
                    placeholder="-- Choose your vehicle --"
                    styles={{
                      control: (provided) => ({
                        ...provided,
                        backgroundColor: '#2a2a2a',
                        borderColor: '#f4cc5d',
                        color: 'white',
                      }),
                      singleValue: (provided) => ({
                        ...provided,
                        color: 'white'
                      }),
                      option: (provided, state) => ({
                        ...provided,
                        backgroundColor: state.isFocused ? '#f4cc5d' : '#1e1e1e',
                        color: state.isFocused ? '#000' : '#fff',
                        cursor: 'pointer',
                      }),
                      menu: (provided) => ({
                        ...provided,
                        backgroundColor: '#1e1e1e',
                      }),
                    }}
                  />
                  <button
                    onClick={handleStartCharging}
                    className={styles.confirmButton}
                    style={{ marginTop: '1rem' }}
                    id="start-charging-button"
                  >
                    Start Charging
                  </button>
                </>
              )}
              {message && (
                <p className={styles.message} id="status-message">
                  {message}
                </p>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
