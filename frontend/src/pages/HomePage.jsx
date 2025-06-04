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
import { useUser } from "../context/UserContext";
import ModalAddCharging from '../components/ModalAddCharging';
import ModalEditCharging from '../components/ModalEditCharging';
import ModalChargingPoints from '../components/ModalChargingPoints';

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
  const { userType } = useUser();
  const [showModal, setShowModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showAddPointModal, setShowAddPointModal] = useState(false);


   const handleNewStation = (newStation) => {
    setStations(prev => [...prev, newStation]);
  };

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
        cityName: station.cityName,
        address: station.address,
        latitude: station.latitude,
        longitude: station.longitude,
        chargingPoints: (station.chargingPoints || []).map((cp) => ({
          ...cp,
          connectors: cp.connectors || [],
        })),
        countryCode: station.countryCode,
        country: station.country || '',
      }));
      setStations(stationsData);
      console.log("Estações carregadas com sucesso:", stationsData);
    })
    .catch((error) => {
      console.error("Erro ao buscar estações:", error.response || error.message);
      if (error.response?.status === 403 || error.response?.status === 401) {
        localStorage.removeItem("token");
        navigate("/"); 
      }
    });
}, [navigate, token]);

  const handleEditButtonClick = () => {
  if (selectedStation) {
    setShowEditModal(true);
  } else {
    alert("Select a charging station to edit.");
  }
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

      <div className={styles.wrapper} id="map-and-details-wrapper">
        <div className={styles.stationDetails} id="station-details-panel">
          <h2 id="station-details-header">Charging Station Details</h2>
          {selectedStation ? (
              <div className={styles.cardScrollable} id="station-card-scrollable">
                <div className={styles.cardContent}>
                  <div className={styles.stationInfo}  id="station-info">
                    <p><strong><FaCity /> City:</strong> {selectedStation.cityName}</p>
                    <p><strong><FaRoad /> Address:</strong> {selectedStation.address}</p>
                    <p><strong>Country:</strong> {selectedStation.country || 'N/A'}</p>
                    <p><strong>Country Code:</strong> {selectedStation.countryCode}</p>
                    <p><strong>Latitude:</strong> {selectedStation.latitude}</p>
                    <p><strong>Longitude:</strong> {selectedStation.longitude}</p>
                  </div>

                  <h3 className={styles.sectionTitle} id="charging-points-header">Charging Points</h3>
                    {selectedStation.chargingPoints && selectedStation.chargingPoints.length > 0 ? (
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
                                <div key={connector.id} className={styles.connectorItem}>
                                  <span><FiZap className={styles.iconSmall} /> <strong>Type:</strong> {connector.connectorType}</span>
                                  <span><FiPower className={styles.iconSmall} /> <strong>Potência:</strong> {connector.ratedPowerKW} kW</span>
                                  <span><TbBatteryCharging2 className={styles.iconSmall} /> <strong>Voltagem:</strong> {connector.voltageV} V</span>
                                  <span><GiElectric className={styles.iconSmall} /> <strong>Corrente:</strong> {connector.currentA} A</span>
                                </div>
                              ))}
                            </div>
                          ) : (
                            <p style={{ fontStyle: 'italic', color: '#666', paddingLeft: '8px' }}>
                              No connectors available for this charging point.
                            </p>
                          )}
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
                  Reserve
                </button>
                        </div>
                      ))
                    ) : (
                      <div
                        className={styles.addChargingPointBox}
                        onClick={() => setShowAddPointModal(true)}
                      >
                        <span className={styles.addIcon}>+</span>
                        <p>Add Charging Point</p>
                      </div>
                    )}
                </div>
                <button className={styles.reserveBtn}>Reservations</button>

                {userType === 'administrator' && (
                  <>
                    <button className={styles.editBtn} onClick={handleEditButtonClick}>Edit</button>
                
                    {selectedStation.chargingPoints &&
                     selectedStation.chargingPoints.length >0 && (
                      <button className={styles.editBtn} onClick={() => setShowAddPointModal(true)}>+ Add Point</button>
                    )}
                  </>
                )}

              </div>
          ) : (
            <p>Selecione uma estação no mapa</p>
          )}

        </div>

        <div className={styles.mapWrapper}>
        <MapContainer
          center={[40.641, -8.653]}
          zoom={14}
          className={styles.map}
        >
          <TileLayer
            attribution='&copy; OpenStreetMap contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          {stations.map((station) => (
            <Marker
              key={station.id}
              position={[station.latitude, station.longitude]}
              icon={customIcon}
              eventHandlers={{
                click: () => setSelectedStation(station),
              }}
            >
              <Popup>
                <div className={styles.popupContent}>
                  <p><FaCity className={styles.popupIcon} /> <strong>City Name:</strong> {station.cityName}</p>
                  <p><strong>Latitude:</strong>{station.latitude}</p>
                  <p><strong>Longitude:</strong>{station.longitude}</p>
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
        </div>

        {userType === 'administrator' && (
        <button
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

        

      </div>
    </div>
  )}
</div>
  );
}