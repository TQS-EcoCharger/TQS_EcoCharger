import { useState, useEffect } from 'react';
import styles from '../css/ModalEditCharging.module.css';
import axios from 'axios';
import CONFIG from '../config';

function ModalEditCharging({ station, onClose, onSuccess }) {
  const [form, setForm] = useState({
    id: "",
    cityName: "",
    address: "",
    latitude: "",
    longitude: "",
    countryCode: "",
    country: ""
  });

  useEffect(() => {
    if (station) {
      setForm({
        id: station.id,
        cityName: station.cityName || "",
        address: station.address || "",
        latitude: station.latitude || "",
        longitude: station.longitude || "",
        countryCode: station.countryCode || "",
        country: station.country || ""
      });
    }
  }, [station]);

  const countryOptions = [
    { code: 'PT', name: 'Portugal' },
    { code: 'ES', name: 'Espanha' },
    { code: 'FR', name: 'França' },
    { code: 'DE', name: 'Alemanha' },
    { code: 'IT', name: 'Itália' },
    { code: 'US', name: 'Estados Unidos' },
  ];

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === 'countryCode') {
      const selected = countryOptions.find(opt => opt.code === value);
      setForm(prev => ({
        ...prev,
        countryCode: value,
        country: selected ? selected.name : ""
      }));
    } else {
      setForm(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = () => {
    console.log("Submitting form:", form);
    const token = localStorage.getItem('token');

    axios.put(`${CONFIG.API_URL}v1/admin/stations/${form.id}`, form, {
      headers: { Authorization: `Bearer ${token}` }
    })
    .then((res) => {
      onSuccess(res.data);
    })
    .catch((err) => {
      console.error("Erro ao editar estação", err);
      alert("Erro ao editar estação");
    });

    
  };

  return (
    <div className={styles.modalBackdrop}>
      <div className={styles.modalContent}>
        <h2>Edit Station</h2>

        <div className={styles.formGroup}>
          <label>City Name</label>
          <input name="cityName" value={form.cityName} onChange={handleChange} />
        </div>

        <div className={styles.formGroup}>
          <label>Address</label>
          <input name="address" value={form.address} onChange={handleChange} />
        </div>

        <div className={styles.formGroup}>
          <label>Latitude</label>
          <input type="number" name="latitude" value={form.latitude} onChange={handleChange} />
        </div>

        <div className={styles.formGroup}>
          <label>Longitude</label>
          <input type="number" name="longitude" value={form.longitude} onChange={handleChange} />
        </div>

        <div className={styles.formGroup}>
          <label>Country Code</label>
          <select name="countryCode" value={form.countryCode} onChange={handleChange}>
            <option value="">-- Selecione --</option>
            {countryOptions.map(({ code, name }) => (
              <option key={code} value={code}>
                {code} - {name}
              </option>
            ))}
          </select>
        </div>

        <div className={styles.formGroup}>
          <label>Country</label>
          <input type="text" name="country" value={form.country} readOnly />
        </div>

        <div className={styles.modalActions}>
          <button onClick={handleSubmit}>Save</button>
          <button onClick={onClose}>Cancel</button>
        </div>
      </div>
    </div>
  );
}

export default ModalEditCharging;
