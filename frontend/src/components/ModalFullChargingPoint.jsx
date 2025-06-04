import { useEffect, useState } from 'react';
import styles from '../css/ModalFullChargingPoint.module.css';
import axios from 'axios';
import { FiZap, FiPower } from 'react-icons/fi';
import { TbBatteryCharging2 } from 'react-icons/tb';
import { GiElectric } from 'react-icons/gi';
import CONFIG from '../config';
import { FiTrash2 } from 'react-icons/fi';

export default function ModalFullChargingPoint({ stationId, onClose, onSuccess, pointToEdit }) {
  const token = localStorage.getItem('token');
  const [error, setError] = useState('');

  console.log('ModalFullChargingPoint mounted with pointToEdit:', pointToEdit);
  const [form, setForm] = useState({
    brand: '',
    available: true,
    pricePerKWh: '',
    pricePerMinute: '',
    connectors: [],
  });

  const [connector, setConnector] = useState({
    connectorType: '',
    ratedPowerKW: '',
    voltageV: '',
    currentA: '',
    currentType: '',
  });

    useEffect(() => {
  if (pointToEdit) {
    setForm({
      brand: pointToEdit.brand || '',
      available: pointToEdit.available ?? true,
      pricePerKWh: pointToEdit.pricePerKWh?.toString() || '',
      pricePerMinute: pointToEdit.pricePerMinute?.toString() || '',
      connectors: pointToEdit.connectors || [],
    });
  }
}, [pointToEdit]);

  const handleFormChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleConnectorChange = (e) => {
    const { name, value } = e.target;
    setConnector(prev => ({ ...prev, [name]: value }));
  };

  const removeConnector = (index) => {
  setForm(prev => ({
    ...prev,
    connectors: prev.connectors.filter((_, i) => i !== index),
  }));
};


const addConnector = () => {
  const { connectorType, ratedPowerKW, voltageV, currentA, currentType } = connector;

  // Validação simples
  if (
    !connectorType.trim() ||
    !ratedPowerKW ||
    !voltageV ||
    !currentA ||
    !currentType.trim()
  ) {
    setError('Preencha todos os campos do conector antes de adicioná-lo.');
    return;
  }

  setError('');
  setForm(prev => ({
    ...prev,
    connectors: [...prev.connectors, connector],
  }));
  setConnector({
    connectorType: '',
    ratedPowerKW: '',
    voltageV: '',
    currentA: '',
    currentType: '',
  });
};

const handleSubmit = () => {
  setError('');

  if (
    !form.brand.trim() ||
    !form.pricePerKWh ||
    !form.pricePerMinute ||
    form.connectors.length === 0
  ) {
    setError('Preencha todos os campos obrigatórios e adicione pelo menos um conector.');
    return;
  }

  for (const conn of form.connectors) {
    if (
      !conn.connectorType.trim() ||
      !conn.ratedPowerKW ||
      !conn.voltageV ||
      !conn.currentA ||
      !conn.currentType.trim()
    ) {
      setError('Todos os campos de cada conector devem estar preenchidos.');
      return;
    }
  }

const payload = pointToEdit
  ? {
      brand: form.brand,
      available: form.available,
      pricePerKWh: parseFloat(form.pricePerKWh),
      pricePerMinute: parseFloat(form.pricePerMinute),
      connectors: form.connectors,
    }
  : {
      point: {
        brand: form.brand,
        available: form.available,
        pricePerKWh: parseFloat(form.pricePerKWh),
        pricePerMinute: parseFloat(form.pricePerMinute),
        connectors: form.connectors,
      },
      station: { id: stationId },
    };


    const url = pointToEdit
      ? `${CONFIG.API_URL}v1/admin/stations/${stationId}/points/${pointToEdit.id}`
      : `${CONFIG.API_URL}v1/points`;

    const method = pointToEdit ? 'put' : 'post';

    axios[method](url, payload, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((res) => {
        onSuccess(res.data);
        onClose();
      })
      .catch((err) => {
        console.error('Erro ao salvar:', err);
        setError('Erro ao salvar o ponto. Tente novamente.');
      });
};



  return (
    <div className={styles.modalBackdrop}>
      <div className={styles.modalLarge}>
        <h2>Add a complete Charging Point</h2>

        <div className={styles.fieldGroup}>
          <label>Brand</label>
          <input type="text" name="brand" value={form.brand} onChange={handleFormChange} />
        </div>

        <div className={styles.fieldGroup}>
          <label>Preço por kWh (€)</label>
          <input type="number" name="pricePerKWh" value={form.pricePerKWh} onChange={handleFormChange} />
        </div>

        <div className={styles.fieldGroup}>
          <label>Preço por Minuto (€)</label>
          <input type="number" name="pricePerMinute" value={form.pricePerMinute} onChange={handleFormChange} />
        </div>

        <h3>Conectors</h3>
        <div className={styles.connectorFields}>
          <select name="connectorType" value={connector.connectorType} onChange={handleConnectorChange}>
            <option value="">Selecione o tipo de conector</option>
            <option value="IEC62196Type2">IEC 62196 Tipo 2</option>
            <option value="IEC62196Type2Outlet">IEC 62196 Tipo 2 (Tomada)</option>
            <option value="CCS2">CCS Tipo 2 (Combo)</option>
            <option value="CHAdeMO">CHAdeMO</option>
          </select>
          <input placeholder="Potência (kW)" name="ratedPowerKW" value={connector.ratedPowerKW} onChange={handleConnectorChange} />
          <input placeholder="Voltagem (V)" name="voltageV" value={connector.voltageV} onChange={handleConnectorChange} />
          <input placeholder="Corrente (A)" name="currentA" value={connector.currentA} onChange={handleConnectorChange} />
          <select name="currentType" value={connector.currentType} onChange={handleConnectorChange}>
            <option value="">Selecione o tipo de corrente</option>
            <option value="AC1">AC1 Monofásico</option>
            <option value="AC3">AC3 Trifásico</option>
            <option value="DC">CC</option>
          </select>
          <button onClick={addConnector}>Add conector</button>
        </div>

        <div className={styles.connectorList}>
        {form.connectors.map((conn, idx) => (
          <div key={`${conn.connectorType}-${conn.ratedPowerKW}-${idx}`} className={styles.connectorItem}>
            <FiZap /> {conn.connectorType} |
            <FiPower /> {conn.ratedPowerKW}kW |
            <TbBatteryCharging2 /> {conn.voltageV}V |
            <GiElectric /> {conn.currentA}A ({conn.currentType})
            <button
              onClick={() => removeConnector(idx)}
              title="Remove connector"
              style={{ marginLeft: '8px', background: 'none', border: 'none', cursor: 'pointer' }}
            >
              <FiTrash2 />
            </button>
          </div>
        ))}
      </div>

        <div className={styles.actions}>
          {error && <div className={styles.errorMessage}>{error}</div>}
          <button onClick={handleSubmit}>Save</button>
          <button onClick={onClose}>Cancel</button>
        </div>
      </div>
    </div>
  );
}