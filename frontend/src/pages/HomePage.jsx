import { useEffect, useState } from 'react';
import axios from 'axios';
import styles from '../css/HomePage.module.css';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import L from 'leaflet';
import CONFIG from '../config';

delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
});

export default function HomePage() {
  const [stations, setStations] = useState([]);
  const [selectedStation, setSelectedStation] = useState(null);


  useEffect(() => {
    axios
      .get(`${CONFIG.API_URL}v1/chargingStations`, { withCredentials: true })
      .then((res) => {
        const stationsData = res.data.map(station => ({
          id: station.id,
          municipality: station.municipality,
          address: station.address,
          latitude: station.latitude,
          longitude: station.longitude,
          chargingPoints: station.chargingPoints || [],
        }));

        setStations(stationsData);
      })
      .catch((error) => {
        console.error('Erro ao buscar estações:', error);
      });
  }, []);

return (
  <div className={styles.wrapper}>
    <div className={styles.sidebar}>
      <h2>Detalhes da Estação</h2>
      {selectedStation ? (
        <>
          <p><strong>{selectedStation.cityName}</strong></p>
          <p>{selectedStation.address}</p>
          <h3>Pontos de Carregamento:</h3>
          <ul>
            {selectedStation.chargingPoints.map((point) => (
              <li key={point.id}>
                {point.brand} - {point.available ? 'Disponível' : 'Indisponível'}
              </li>
            ))}
          </ul>
        </>
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
        attribution='&copy; OpenStreetMap'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {stations.map((station) => (
        <Marker
          key={station.id}
          position={[station.latitude, station.longitude]}
          eventHandlers={{
            click: () => {
              setSelectedStation(station);
            },
          }}
        >
          <Popup>{station.cityName}</Popup>
        </Marker>
      ))}
    </MapContainer>
  </div>
);
}