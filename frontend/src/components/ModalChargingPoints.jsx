import { useState, useEffect, useCallback } from 'react';
import styles from '../css/ModalAddChargingPoint.module.css';
import CONFIG from '../config';
import axios from 'axios';
import ModalFullChargingPoint from './ModalFullChargingPoint';
import { FiZap, FiEdit } from 'react-icons/fi';
import { BsCheckCircle, BsXCircle } from 'react-icons/bs';

export default function ModalAddChargingPoint({ stationId, onClose, onSuccess }) {
  const token = localStorage.getItem('token');
  const [showFormSlotIndex, setShowFormSlotIndex] = useState(null);
  const [formData, setFormData] = useState({ brand: '', available: true });
  const [chargingPoints, setChargingPoints] = useState([]);
  const [showFullModal, setShowFullModal] = useState(false);
  const [editingPoint, setEditingPoint] = useState(null);
  const totalSlots = 8;

  const handleChargingPoints = useCallback(() => {
    axios
      .get(`${CONFIG.API_URL}v1/points/station/${stationId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setChargingPoints(res.data);
      })
      .catch((err) => {
        console.error('Failed to fetch charging points:', err);
      });
  }, [stationId, token]);

  useEffect(() => {
    if (stationId) {
      handleChargingPoints();
    }
  }, [stationId, handleChargingPoints]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = () => {
    const requestBody = {
      point: formData,
      station: { id: stationId },
    };

    axios
      .post(`${CONFIG.API_URL}v1/points`, requestBody, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((res) => {
        setFormData({ brand: '', available: true });
        setShowFormSlotIndex(null);
        handleChargingPoints();
        onSuccess(res.data);
      })
      .catch((err) => {
        console.error('Failed to add charging point:', err);
        alert('Failed to add charging point');
      });
  };

  return (
    <div className={styles.modalBackdrop}>
      <div className={styles.modalContent}>
        <h2>Add Charging Point</h2>

        <div className={styles.slotGrid}>
          {Array.from({ length: totalSlots }).map((_, index) => {
            const point = chargingPoints[index];

            return (
              <div key={point?.id ?? index} className={styles.slotBox}>
                {point ? (
                  <div className={styles.occupiedSlot}>
                    <FiZap className={styles.icon} />
                    <p>
                      <strong>{point.brand}</strong>
                    </p>
                    {point.available ? (
                      <BsCheckCircle className={styles.availableIcon} title="Available" />
                    ) : (
                      <BsXCircle className={styles.unavailableIcon} title="Unavailable" />
                    )}
                    <FiEdit
                      className={styles.editIcon}
                      title="Edit charging point"
                      onClick={() => {
                        setEditingPoint(point);
                        setShowFullModal(true);
                      }}
                    />
                  </div>
                ) : showFormSlotIndex === index ? (
                  <div className={styles.slotForm}>
                    <input
                      type="text"
                      placeholder="Brand"
                      name="brand"
                      value={formData.brand}
                      onChange={handleChange}
                    />
                    <label className={styles.checkboxLabel}>
                      <input
                        type="checkbox"
                        name="available"
                        checked={formData.available}
                        onChange={handleChange}
                      />
                      Available
                    </label>
                    <div className={styles.formActions}>
                      <button onClick={handleSubmit}>Save</button>
                      <button onClick={() => setShowFormSlotIndex(null)}>Cancel</button>
                    </div>
                  </div>
                ) : (
                  <button
                    className={styles.addSlotButton}
                    onClick={() => setShowFullModal(true)}
                  >
                    +
                  </button>
                )}
              </div>
            );
          })}
        </div>

        <div className={styles.bottomActions}>
          <button className={styles.cancelBtn} onClick={onClose}>
            Close
          </button>
        </div>
      </div>

      {showFullModal && (
        <ModalFullChargingPoint
          stationId={stationId}
          onClose={() => setShowFullModal(false)}
          onSuccess={(newPoint) => {
            onSuccess(newPoint);
            setShowFullModal(false);
            handleChargingPoints();
          }}
          pointToEdit={editingPoint}
        />
      )}
    </div>
  );
}
