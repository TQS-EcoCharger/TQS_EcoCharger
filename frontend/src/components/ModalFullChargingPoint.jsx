import { useEffect, useState } from 'react';
import styles from '../css/ModalFullChargingPoint.module.css';
import axios from 'axios';
import { FiZap, FiPower, FiTrash2 } from 'react-icons/fi';
import { TbBatteryCharging2 } from 'react-icons/tb';
import { GiElectric } from 'react-icons/gi';
import CONFIG from '../config';

export default function ModalFullChargingPoint({ stationId, onClose, onSuccess, pointToEdit }) {
  const token = localStorage.getItem('token');
  const [error, setError] = useState('');

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

    if (!connectorType || !ratedPowerKW || !voltageV || !currentA || !currentType) {
      setError('Fill in all connector fields before adding.');
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
      setError('Fill all required fields and add at least one connector.');
      return;
    }

    for (const conn of form.connectors) {
      if (
        !conn.connectorType ||
        !conn.ratedPowerKW ||
        !conn.voltageV ||
        !conn.currentA ||
        !conn.currentType
      ) {
        setError('All connector fields must be completed.');
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
        console.error('Error saving point:', err);
        setError('Error saving point. Please try again.');
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
          <label>Price per kWh (€)</label>
          <input type="number" name="pricePerKWh" value={form.pricePerKWh} onChange={handleFormChange} />
        </div>

        <div className={styles.fieldGroup}>
          <label>Price per Minute (€)</label>
          <input type="number" name="pricePerMinute" value={form.pricePerMinute} onChange={handleFormChange} />
        </div>

        <h3>Connectors</h3>
        <div className={styles.connectorFields}>
          <select name="connectorType" value={connector.connectorType} onChange={handleConnectorChange}>
            <option value="">Select connector type</option>
            <option value="IEC62196Type2">IEC 62196 Type 2</option>
            <option value="IEC62196Type2Outlet">IEC 62196 Type 2 (Socket)</option>
            <option value="CCS2">CCS Type 2 (Combo)</option>
            <option value="CHAdeMO">CHAdeMO</option>
          </select>
          <input placeholder="Power (kW)" name="ratedPowerKW" value={connector.ratedPowerKW} onChange={handleConnectorChange} />
          <input placeholder="Voltage (V)" name="voltageV" value={connector.voltageV} onChange={handleConnectorChange} />
          <input placeholder="Current (A)" name="currentA" value={connector.currentA} onChange={handleConnectorChange} />
          <select name="currentType" value={connector.currentType} onChange={handleConnectorChange}>
            <option value="">Select current type</option>
            <option value="AC1">AC1 Single Phase</option>
            <option value="AC3">AC3 Three Phase</option>
            <option value="DC">DC</option>
          </select>
          <button onClick={addConnector}>Add connector</button>
        </div>

        <div className={styles.connectorList}>
          {form.connectors.map((conn) => (
  <div
    key={`${conn.connectorType}-${conn.ratedPowerKW}-${conn.voltageV}-${conn.currentA}-${conn.currentType}`}
    className={styles.connectorItem}
  >
    <FiZap /> {conn.connectorType} |
    <FiPower /> {conn.ratedPowerKW}kW |
    <TbBatteryCharging2 /> {conn.voltageV}V |
    <GiElectric /> {conn.currentA}A ({conn.currentType})
    <button
      onClick={() =>
        removeConnector(
          form.connectors.findIndex((c) =>
            c.connectorType === conn.connectorType &&
            c.ratedPowerKW === conn.ratedPowerKW &&
            c.voltageV === conn.voltageV &&
            c.currentA === conn.currentA &&
            c.currentType === conn.currentType
          )
        )
      }
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
