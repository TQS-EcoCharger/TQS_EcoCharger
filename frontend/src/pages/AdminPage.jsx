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
import { FaRoad } from 'react-icons/fa';
import { FaCity } from 'react-icons/fa';
import { BsPlug, BsCheckCircle, BsXCircle } from 'react-icons/bs';
import { TbBatteryCharging2 } from 'react-icons/tb';
import { GiElectric } from 'react-icons/gi';
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


export default function AdminPage() {

const [stations, setStations] = useState([]);
  const [selectedStation, setSelectedStation] = useState(null);
  const token = localStorage.getItem('token');
  const navigate = useNavigate();

 useEffect(() => {
  if (!token) {
    console.warn("Nenhum token encontrado. Redirecionando para login...");
    navigate("/");
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
      console.error("Erro ao buscar estações:", error.response || error.message);
      if (error.response?.status === 403 || error.response?.status === 401) {
        localStorage.removeItem("token");
        navigate("/");
      }
    });
}, [navigate, token]);


    return (
            <div className={styles.page}>
              <Sidebar />
        
              <div className={styles.wrapper}>
                <div className={styles.stationDetails}>
                  <h2>Detalhes da Estação</h2>
                  {selectedStation ? (
                      <div className={styles.cardScrollable}>
                        <div className={styles.cardContent}>
                          <div className={styles.stationInfo}>
                            <p><strong><FaCity /> Município:</strong> {selectedStation.municipality}</p>
                            <p><strong><FaRoad /> Morada:</strong> {selectedStation.address}</p>
                          </div>
        
                          <h3 className={styles.sectionTitle}>Pontos de Carregamento</h3>
                          {selectedStation.chargingPoints.map((point) => (
                            <div key={point.id} className={styles.chargingCard}>
                              <div className={styles.chargingHeader}>
                                <BsPlug className={styles.icon} />
                                <span><strong>{point.brand}</strong></span>
                                {point.available ? (
                                  <BsCheckCircle className={styles.available} title="Disponível" />
                                ) : (
                                  <BsXCircle className={styles.unavailable} title="Indisponível" />
                                )}
                              </div>
                              {point.connectors.length > 0 ? (
                                <div className={styles.connectorList}>
                                  {point.connectors.map((connector) => (
                                    <div key={connector.id} className={styles.connectorItem}>
                                      <span><FiZap className={styles.iconSmall} /> <strong>Tipo:</strong> {connector.connectorType}</span>
                                      <span><FiPower className={styles.iconSmall} /> <strong>Potência:</strong> {connector.ratedPowerKW} kW</span>
                                      <span><TbBatteryCharging2 className={styles.iconSmall} /> <strong>Voltagem:</strong> {connector.voltageV} V</span>
                                      <span><GiElectric className={styles.iconSmall} /> <strong>Corrente:</strong> {connector.currentA} A</span>
                                    </div>
                                  ))}
                                </div>
                              ) : (
                                <p style={{ fontStyle: 'italic', color: '#666', paddingLeft: '8px' }}>
                                  Nenhum conector disponível.
                                </p>
                              )}
                            </div>
                          ))}
                        </div>
                        <button className={styles.reserveBtn}>Reservar</button>
                      </div>
                  ) : (
                    <p>Selecione uma estação no mapa</p>
                  )}
        
                </div>
        
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
                          <p><FaCity className={styles.popupIcon} /> <strong>Município:</strong> {station.municipality}</p>
                          <p><strong>Latitude:</strong>{station.latitude}</p>
                          <p><strong>Longitude:</strong>{station.longitude}</p>
                        </div>
                      </Popup>
                    </Marker>
                  ))}
                </MapContainer>
              </div>
            </div>
    );
    }

