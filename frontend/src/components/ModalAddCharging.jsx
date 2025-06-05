import { useState } from 'react';
import styles from '../css/ModalAddCharging.module.css';
import axios from 'axios';
import CONFIG from '../config';

export default function ModalAddCharging({ onClose, onSuccess }) {
  const [formData, setFormData] = useState({
    cityName: '',
    address: '',
    latitude: '',
    longitude: '',
    countryCode: '',
    country: ''
  });

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
      setFormData(prev => ({
        ...prev,
        countryCode: value,
        country: selected ? selected.name : ''
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem('token');
      const response = await axios.post(`${CONFIG.API_URL}v1/chargingStations`, formData, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      onSuccess(response.data);
      onClose();
    } catch (err) {
      console.error('Erro ao adicionar estação:', err.response || err.message);
      alert("Erro ao adicionar estação.");
    }
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <h2>Adicionar Estação</h2>
        <form onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label htmlFor="cityName">Município</label>
            <input id="cityName" type="text" name="cityName" value={formData.cityName} onChange={handleChange} required />
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="address">Morada</label>
            <input id="address" type="text" name="address" value={formData.address} onChange={handleChange} required />
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="latitude">Latitude</label>
            <input id="latitude" type="number" step="any" name="latitude" value={formData.latitude} onChange={handleChange} required />
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="longitude">Longitude</label>
            <input id="longitude" type="number" step="any" name="longitude" value={formData.longitude} onChange={handleChange} required />
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="countryCode">Código do País</label>
            <select id="countryCode" name="countryCode" value={formData.countryCode} onChange={handleChange} required>
              <option value="">-- Selecione --</option>
              {countryOptions.map(({ code, name }) => (
                <option key={code} value={code}>{code} - {name}</option>
              ))}
            </select>
          </div>

          <div className={styles.formGroup}>
            <label htmlFor="country">País</label>
            <input id="country" type="text" name="country" value={formData.country} readOnly />
          </div>

          <div className={styles.actions}>
            <button
              id="submit-add-station"
              type="submit"
              disabled={!formData.cityName || !formData.address || !formData.latitude || !formData.longitude || !formData.countryCode}
            >
              Salvar
            </button>
            <button
              type="button"
              onClick={onClose}
              className={styles.cancelBtn}
              id="cancel-add-station"
            >
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
