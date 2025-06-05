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
import { useUser } from "../context/UserProvider.jsx";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCar } from '@fortawesome/free-solid-svg-icons';


import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import Chargingicon from '../../public/ChargingStation.png';
import ModalAddCharging from '../components/ModalAddCharging';
import ModalEditCharging from '../components/ModalEditCharging';
import ModalChargingPoints from '../components/ModalChargingPoints';
import ChargingPointReservations from '../components/ChargingPointReservations';

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
  const [currentReservations, setCurrentReservations] = useState([]);
  const [selectedStation, setSelectedStation] = useState(null);
  const [selectedPoint, setSelectedPoint] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [startTime, setStartTime] = useState(new Date());
  const [endTime, setEndTime] = useState(new Date());
  const [message, setMessage] = useState('');
  const [userLocation, setUserLocation] = useState(null);
  const { userType } = useUser();
  const me = JSON.parse(localStorage.getItem('me')) || {};

  const [existingReservations, setExistingReservations] = useState([]);

  const token = localStorage.getItem('token');
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showAddPointModal, setShowAddPointModal] = useState(false);

  const handleNewStation = (newStation) => {
    setStations(prev => [...prev, newStation]);
  };

  useEffect(() => {

    const fetchCurrentReservations = async () => {
      console.log(`${CONFIG.API_URL}v1/reservation`);
      const response = await axios.get(`http://localhost:8080/api/v1/reservation`, 
        {
          headers: {
            "Authorization": `Bearer ${token}`
          }
        }
      );
      setCurrentReservations(response.data);
      console.log("Current reservations fetched successfully:", response.data);
    }

    const fetchSelf = async () => {
      if (!localStorage.getItem("me")) {
        try {
          console.log(CONFIG.API_URL);
          const response = await axios.get(`${CONFIG.API_URL}auth/me`, {
            headers: {
              "Authorization": `Bearer ${token}`
            }
          });
          localStorage.setItem("me", JSON.stringify(response.data));
        } catch (error) {
          console.error("Error fetching user data:", error);
        }
      }
    };
    fetchSelf();
    fetchCurrentReservations();
  }, []);

  useEffect(() => {
    if (!navigator.geolocation) return userLocation;
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setUserLocation({
          lat: position.coords.latitude,
          lng: position.coords.longitude,
        });
      },
      (error) => console.error('Geolocation error:', error)
    );
  }, []);

  useEffect(() => {
    if (!token) {
      navigate('/');
      return;
    }

    axios.get(`${CONFIG.API_URL}v1/chargingStations`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => {
        const stationsData = res.data.map((station) => ({
          ...station,
          chargingPoints: (station.chargingPoints || []).map((cp) => ({
            ...cp,
            connectors: cp.connectors || [],
          })),
        }));
        setStations(stationsData);
      })
      .catch((error) => {
        if ([401, 403].includes(error.response?.status)) {
          localStorage.removeItem("token");
          navigate("/");
        }
        console.error("Failed to fetch stations:", error);
      });
  }, [navigate, token]);

  const handleEditButtonClick = () => {
    if (selectedStation) setShowEditModal(true);
    else alert("Select a charging station to edit.");
  };

  

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

    axios.post(`${CONFIG.API_URL}v1/reservation`, payload, {
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
        setCurrentReservations((prev) => [
          ...prev,
          {
            id: Date.now(), // Temporary ID for optimistic UI update
            chargingPoint: selectedPoint,
            startTime: startTime.toISOString(),
            endTime: endTime.toISOString(),
            status: 'PENDING', // Assuming new reservations start as PENDING
          },
        ]);
      })
      .catch((error) => {
        setMessage(error.response?.data || error.message);
      });
  };

  return (
    <div className={styles.page} id="homepage">
      <Sidebar />
      <div className={styles.wrapper} id="map-and-details-wrapper">
        <div className={styles.stationDetails} id="station-details-panel">
          <h2 id="station-details-header">Charging Station Details</h2>
          {selectedStation ? (
            <div className={styles.cardScrollable} id="station-card-scrollable">
              <div className={styles.cardContent}>
                <div className={styles.stationInfo} id="station-info">
                  <p><strong><FaCity /> City:</strong> {selectedStation.cityName}</p>
                  <p><strong><FaRoad /> Address:</strong> {selectedStation.address}</p>
                  <p><strong>Country:</strong> {selectedStation.country || 'N/A'}</p>
                  <p><strong>Country Code:</strong> {selectedStation.countryCode}</p>
                  <p><strong>Latitude:</strong> {selectedStation.latitude}</p>
                  <p><strong>Longitude:</strong> {selectedStation.longitude}</p>
                </div>

                <h3 className={styles.sectionTitle} id="charging-points-header">Charging Points</h3>
                {selectedStation.chargingPoints?.length > 0 ? (
                  selectedStation.chargingPoints.map((point) => (
                    <div key={point.id} className={styles.chargingCard} id={`charging-point-${point.id}`}>
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
                            <div key={connector.id} className={styles.connectorItem}  id={`connector-${connector.id}`}>
                              <span><FiZap className={styles.iconSmall} /> <strong>Type:</strong> {connector.connectorType}</span>
                              <span><FiPower className={styles.iconSmall} /> <strong>Power:</strong> {connector.ratedPowerKW} kW</span>
                              <span><TbBatteryCharging2 className={styles.iconSmall} /> <strong>Voltage:</strong> {connector.voltageV} V</span>
                              <span><GiElectric className={styles.iconSmall} /> <strong>Current:</strong> {connector.currentA} A</span>
                            </div>
                          ))}
                        </div>
                      ) : (
                        <p className={styles.noConnectors}>No connectors available.</p>
                      )}
                      {point.available ? (
                      <div className={styles.buttonRow}>
                        <ChargingPointReservations reservations={currentReservations} chargingPointId={point.id} />
                        <button
                          className={styles.reserveBtn}
                          id={`reserve-button-${point.id}`}
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
                          <FontAwesomeIcon icon={faCar} className={styles.faicon} />
                          Reserve
                        </button>
                      </div>
                      ) : (
                        <></>
                      )}
                    </div>
                  ))
                ) : (userType === 'administrator' || (userType === 'chargingOperator' && me.chargingStations?.some(station => station.id === selectedStation.id))) ? (
                  <div className={styles.addChargingPointBox} onClick={() => setShowAddPointModal(true)}>
                    <span className={styles.addIcon}>+</span>
                    <p>Add Charging Point</p>
                  </div>
                ) : (
                  <div className={styles.addChargingPointBox}>
                    <p className={styles.noChargingPoints}>No charging points available.</p>
                  </div>
                )}
                {userType === 'administrator' || (userType === 'chargingOperator' && me.chargingStations?.some(station => station.id === selectedStation.id)) && (
                  <>
                    <button className={styles.editBtn} onClick={handleEditButtonClick}>Edit</button>
                    {selectedStation.chargingPoints?.length > 0 && (
                      <button className={styles.editBtn} onClick={() => setShowAddPointModal(true)}>+ Add Point</button>
                    )}
                  </>
                )}
              </div>
            </div>
          ) : (
            <p id="select-station-placeholder">Select a station on the map</p>)}
        </div>

        <div className={styles.mapWrapper}>
          <MapContainer center={[40.641, -8.653]} zoom={14} className={styles.map}>
            <TileLayer
              attribution='&copy; OpenStreetMap contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
              {stations.map((station, index) => (
                <Marker
                  key={station.id}
                  position={[station.latitude, station.longitude]}
                  icon={customIcon}
                  eventHandlers={{
                    click: () => setSelectedStation(station),
                    add: (e) => {
                      const markerEl = e.target.getElement(); // DOM element
                      if (markerEl) {
                        markerEl.setAttribute("id", `marker-${index + 1}`); // indexado a partir de 1
                      }
                    }
                  }}
                >
                  <Popup data-testid={`marker-popup-${station.id}`}>
                    <div className={styles.popupContent}>
                      <p><FaCity className={styles.popupIcon} /> <strong>City Name:</strong> {station.cityName}</p>
                      <p><strong>Latitude:</strong> {station.latitude}</p>
                      <p><strong>Longitude:</strong> {station.longitude}</p>
                    </div>
                  </Popup>
                </Marker>
              ))}

          </MapContainer>
        </div>

        {userType === 'administrator' && (
          <button
            id="add-charging-station-button"
            className={styles.addStationBtn}
            onClick={() => setShowModal(true)}
          >
            + Add Charging Station
          </button>
        )}

        {showModal && (
          <ModalAddCharging onClose={() => setShowModal(false)} onSuccess={handleNewStation} />
        )}

        {showEditModal && (
          <ModalEditCharging
            station={selectedStation}
            onClose={() => setShowEditModal(false)}
            onSuccess={(updatedStation) => {
              setStations((prev) =>
                prev.map((s) => (s.id === updatedStation.id ? updatedStation : s))
              );
              setSelectedStation(updatedStation);
              setShowEditModal(false);
            }}
          />
        )}

        {showAddPointModal && selectedStation && (
          <ModalChargingPoints
            stationId={selectedStation.id}
            onClose={() => setShowAddPointModal(false)}
            onSuccess={(newPoint) => {
              const updatedStation = {
                ...selectedStation,
                chargingPoints: [...selectedStation.chargingPoints, newPoint]
              };
              setStations(prev =>
                prev.map(s => (s.id === updatedStation.id ? updatedStation : s))
              );
              setSelectedStation(updatedStation);
            }}
          />
        )}

        {isModalOpen && (
          <div className={styles.modalOverlay} id="reservation-modal-overlay">
            <div className={styles.modal} id="reservation-modal">
              <h2 id="modal-title">Make a Reservation</h2>
              <p><strong>Point:</strong> {selectedPoint?.brand}</p>
              {existingReservations.length > 0 && (
                <div className={styles.existingReservations} id="existing-reservations">
                  <h2>Current Reservations:</h2>
                  <ul className={styles.reservationList}>
                    {existingReservations.map((res, index) => (
                      <li key={res.id} className={styles.reservationItem} id={`reservation-${res.id}`}>
                        <span><strong>Reservation {index + 1}</strong></span><br />
                        <span><strong>Start:</strong> {new Date(res.startTime).toLocaleString()}</span><br />
                        <span><strong>End:</strong> {new Date(res.endTime).toLocaleString()}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
              <label htmlFor="start-time-picker">Start:</label>
              <DatePicker
                selected={startTime}
                onChange={(date) => setStartTime(date)}
                showTimeSelect
                timeIntervals={15}
                dateFormat="Pp"
                timeCaption="Time"
                className={styles.datePicker}
                id="start-time-picker"
              />
              <label htmlFor="end-time-picker">End:</label>
              <DatePicker
                selected={endTime}
                onChange={(date) => setEndTime(date)}
                showTimeSelect
                timeIntervals={15}
                dateFormat="Pp"
                timeCaption="Time"
                className={styles.datePicker}
                id="end-time-picker"
              />
              <div className={styles.modalButtons}>
                <button onClick={handleReservation} className={styles.confirmButton} id="confirm-reservation-button">Reserve</button>
                <button onClick={() => setIsModalOpen(false)} className={styles.cancelButton} id="cancel-reservation-button">Cancel</button>
              </div>
              {message && <p className={styles.message} id="reservation-message">{message}</p>}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}