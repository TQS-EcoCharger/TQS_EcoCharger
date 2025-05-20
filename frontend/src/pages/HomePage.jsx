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

  useEffect(() => {
    axios
      .get(`${CONFIG.API_URL}v1/chargingStations`, { withCredentials: true })
      .then((res) => {
        const stationsData = res.data.map(station => ({
          id: station.id,
          cityName: station.cityName,     
          address: station.address,
          latitude: station.latitude,
          longitude: station.longitude,
        }));

        setStations(stationsData);
      })
      .catch((error) => {
        console.error('Error fetching stations:', error);
      });
  }, []);

  return (
    <div className={styles.container}>
      <h1 className={styles.title}>Mapa de Estações de Carregamento</h1>
      <MapContainer
        center={[40.5, -8.5]}
        zoom={7}
        className={styles.map}
      >
        <TileLayer
          attribution='&copy; OpenStreetMap'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        {stations.map(station => (
          <Marker
            key={station.id}
            position={[station.latitude, station.longitude]}
          >
            <Popup>
              <strong>{station.cityName}</strong><br />
              {station.address}
            </Popup>
          </Marker>
        ))}
      </MapContainer>
    </div>
  );
}
