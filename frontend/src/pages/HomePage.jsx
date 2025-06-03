import { useEffect, useState } from 'react';
import axios from 'axios';
import styles from '../css/HomePage.module.css';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import CONFIG from '../config';
import Sidebar from '../components/Sidebar';
import { useNavigate } from 'react-router-dom';

import { FiZap, FiPower } from 'react-icons/fi';
import { FaRoad, FaCity } from 'react-icons/fa';
import { BsPlug, BsCheckCircle, BsXCircle } from 'react-icons/bs';
import { TbBatteryCharging2 } from 'react-icons/tb';
import { GiElectric } from 'react-icons/gi';

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import Chargingicon from '../../public/ChargingStation.png';

const customIcon = new L.Icon({
  iconUrl: Chargingicon,
  iconSize: [40, 45],
  iconAnchor: [17, 45],
  popupAnchor: [0, -40],
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  shadowSize: [41, 41],
  shadowAnchor: [13, 41],
});

export default function HomePage() {
  const [stations, setStations] = useState([]);
  const [selectedStation, setSelectedStation] = useState(null);
  const [selectedPoint, setSelectedPoint] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [startTime, setStartTime] = useState(new Date());
  const [endTime, setEndTime] = useState(new Date());
  const [message, setMessage] = useState('');
  const [userLocation, setUserLocation] = useState(null);

  const [existingReservations, setExistingReservations] = useState([]);

  const token = localStorage.getItem('token');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchSelf = async () => {
      if (localStorage.getItem("me") === null) {
        try {
          const response = await axios.get(`${CONFIG.API_URL}auth/me`, {
            headers: {
              "Authorization": localStorage.getItem("token")
            }
          });
          console.log("User data fetched successfully:", response.data);
          localStorage.setItem("me", JSON.stringify(response.data));
        } catch (error) {
          console.error("Error fetching user data:", error);
        }
      } else {
        console.log("User data already exists in localStorage.");
      }
    };

    fetchSelf();
  }, []);

  useEffect(() => {
  if (!navigator.geolocation) {
    console.warn('Geolocation is not supported by your browser.');
    return;
  }

  navigator.geolocation.getCurrentPosition(
    (position) => {
      setUserLocation({
        lat: position.coords.latitude,
        lng: position.coords.longitude,
      });
    },
    (error) => {
      console.error('Error getting user location:', error);
    }
  );
}, []);


  useEffect(() => {
    if (!token) {
      console.warn('No token found. Redirecting to login...');
      navigate('/');
      return;
    }

    axios
      .get(`${CONFIG.API_URL}v1/chargingStations`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        const stationsData = res.data.map((station) => ({
          id: station.id,
          municipality: station.cityName,
          address: station.address,
          latitude: station.latitude,
          longitude: station.longitude,
          chargingPoints: (station.chargingPoints || []).map((cp) => ({
            ...cp,
            connectors: cp.connectors || [],
          })),
        }));
        setStations(stationsData);
      })
      .catch((error) => {
        console.error('Error fetching stations:', error.response || error.message);
        if (error.response?.status === 403 || error.response?.status === 401) {
          localStorage.removeItem('token');
          navigate('/');
        }
      });
  }, [navigate, token]);

  const handleReservation = () => {
    if (!selectedPoint || !startTime || !endTime) {
      setMessage('Please fill in all fields.');
      return;
    }

    const me = JSON.parse(localStorage.getItem('me'));
    const payload = {
      userId: parseInt(me.id),
      chargingPointId: selectedPoint.id,
      startTime: startTime.toISOString(),
      endTime: endTime.toISOString(),
    };

    axios
      .post(`${CONFIG.API_URL}v1/reservation`, payload, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(() => {
        setMessage('Reservation successfully created!');
        setTimeout(() => {
          setIsModalOpen(false);
          setSelectedPoint(null); 
          setMessage('');
          setStartTime(new Date());
          setEndTime(new Date());
        }, 2000);
      })
      .catch((error) => {
        console.error('Reservation error:', error.response || error.message);
        setMessage((error.response?.data || error.message));
      });
  };

  return (
    <div className={styles.page} id="homepage">
      <Sidebar />

      <div className={styles.wrapper}>
        <div className={styles.stationDetails}>
          <h2>Station Details</h2>
          {selectedStation ? (
            <div className={styles.cardScrollable}>
              <div className={styles.cardContent}>
                <div className={styles.stationInfo}>
                  <p><strong><FaCity /> Municipality:</strong> {selectedStation.municipality}</p>
                  <p><strong><FaRoad /> Address:</strong> {selectedStation.address}</p>
                </div>

                <h3 className={styles.sectionTitle}>Charging Points</h3>
                {selectedStation.chargingPoints.map((point) => (
                  <div key={point.id} className={styles.chargingCard}>
                    <div className={styles.chargingHeader}>
                      <BsPlug className={styles.icon} />
                      <span><strong>{point.brand}</strong></span>
                      {point.available ? (
                        <BsCheckCircle className={styles.available} title="Available" />
                      ) : (
                        <BsXCircle className={styles.unavailable} title="Unavailable" />
                      )}
                    </div>
                    {point.connectors.length > 0 ? (
                      <div className={styles.connectorList}>
                        {point.connectors.map((connector) => (
                          <div key={connector.id} className={styles.connectorItem}>
                            <span><FiZap className={styles.iconSmall} /> <strong>Type:</strong> {connector.connectorType}</span>
                            <span><FiPower className={styles.iconSmall} /> <strong>Power:</strong> {connector.ratedPowerKW} kW</span>
                            <span><TbBatteryCharging2 className={styles.iconSmall} /> <strong>Voltage:</strong> {connector.voltageV} V</span>
                            <span><GiElectric className={styles.iconSmall} /> <strong>Current:</strong> {connector.currentA} A</span>
                          </div>
                        ))}
                      </div>
                    ) : (
                      <p style={{ fontStyle: 'italic', color: '#666', paddingLeft: '8px' }}>
                        No connectors available.
                      </p>
                    )}
                    <button
                      className={styles.reserveBtn}
                      onClick={() => {
                        setSelectedPoint(point);
                        setIsModalOpen(true);

                        axios
                          .get(`${CONFIG.API_URL}v1/reservation/point/${point.id}/active`, {
                            headers: { Authorization: `Bearer ${token}` },
                          })
                          .then((res) => setExistingReservations(res.data))
                          .catch((err) => {
                            console.error('Failed to fetch existing reservations:', err);
                            setExistingReservations([]);
                          });
                      }}
                    >
                      Reserve
                    </button>
                  </div>
                ))}
              </div>
            </div>
          ) : (
            <p>Select a station on the map</p>
          )}
        </div>

        <MapContainer center={[40.641, -8.653]} zoom={14} className={styles.map}>
          {userLocation && (
            <Marker position={[userLocation.lat, userLocation.lng]}>
              <Popup>
                <strong>You are here</strong>
              </Popup>
            </Marker>
          )}
          <TileLayer
            attribution='&copy; OpenStreetMap contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {stations.map((station) => (
            <Marker
              key={station.id}
              position={[station.latitude, station.longitude]}
              icon={customIcon}
              eventHandlers={{ click: () => setSelectedStation(station) }}
            >
              <Popup>
                <div className={styles.popupContent}>
                  <p><FaCity className={styles.popupIcon} /> <strong>Municipality:</strong> {station.municipality}</p>
                  <p><strong>Latitude:</strong> {station.latitude}</p>
                  <p><strong>Longitude:</strong> {station.longitude}</p>
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <h2>Make a Reservation</h2>
            <p><strong>Point:</strong> {selectedPoint?.brand}</p>
            {existingReservations.length > 0 && (
              <div className={styles.existingReservations}>
                <h2 style={{marginTop:"0px"}}>Current Reservations:</h2>
                <ul className={styles.reservationList}>
                  {existingReservations.map((res, index) => (
                    <li key={res.id} className={styles.reservationItem}>
                      <span><strong>Reservation {index + 1}</strong></span><br />
                      <span><strong>Start:</strong> {new Date(res.startTime).toLocaleString()}</span><br />
                      <span><strong>End:</strong> {new Date(res.endTime).toLocaleString()}</span>
                    </li>
                  ))}
                </ul>
              </div>
            )}

            <label>Start:</label>
            <DatePicker
              selected={startTime}
              onChange={(date) => setStartTime(date)}
              showTimeSelect
              timeIntervals={15}
              dateFormat="Pp"
              timeCaption="Time"
              className={styles.datePicker}
            />

            <label>End:</label>
            <DatePicker
              selected={endTime}
              onChange={(date) => setEndTime(date)}
              showTimeSelect
              timeIntervals={15}
              dateFormat="Pp"
              timeCaption="Time"
              className={styles.datePicker}
            />

            <div className={styles.modalButtons}>
              <button onClick={handleReservation} className={styles.confirmButton}>
                Reserve
              </button>
              <button onClick={() => setIsModalOpen(false)} className={styles.cancelButton}>Cancel</button>
            </div>

            {message && <p className={styles.message}>{message}</p>}
          </div>
        </div>
      )}
    </div>
  );
}