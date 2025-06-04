import { useState, useEffect } from 'react';
import styles from '../css/ModalAddChargingPoint.module.css';
import CONFIG from '../config';
import axios from 'axios';
import ModalFullChargingPoint from './ModalFullChargingPoint';
import { FiZap } from 'react-icons/fi';
import { BsCheckCircle, BsXCircle } from 'react-icons/bs';
import { FiEdit } from 'react-icons/fi';

export default function ModalAddChargingPoint({ stationId, onClose, onSuccess }) {
  const token = localStorage.getItem('token');
  const [showFormSlotIndex, setShowFormSlotIndex] = useState(null); 
  const [formData, setFormData] = useState({ brand: '', available: true });
  const [chargingPoints, setChargingPoints] = useState([]);
  const [showFullModal, setShowFullModal] = useState(false);
  const totalSlots = 8;
  const [editingPoint, setEditingPoint] = useState(null);


  useEffect(() => {
    if (stationId) {
      handleChargingPoints();
    }
  }, [stationId]);

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
      station: { id: stationId }
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
        console.error('Erro ao adicionar ponto:', err);
        alert('Erro ao adicionar ponto');
      });
  };

  const handleChargingPoints = () => {
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
        console.error('Erro ao buscar pontos de carregamento:', err);
      });
  };

  return (
    <div className={styles.modalBackdrop}>
      <div className={styles.modalContent}>
        <h2>Adicionar Ponto de Carregamento</h2>

        <div className={styles.slotGrid}>
          {Array.from({ length: totalSlots }).map((_, index) => {
            const point = chargingPoints[index];

            return (
              <div key={index} className={styles.slotBox}>
                {point ? (
                  <div className={styles.occupiedSlot}>
                    <FiZap className={styles.icon} />
                    <p><strong>{point.brand}</strong></p>
                    {point.available ? (
                      <BsCheckCircle className={styles.availableIcon} title="Disponível" />
                    ) : (
                      <BsXCircle className={styles.unavailableIcon} title="Indisponível" />
                    )}
                    <FiEdit
                      className={styles.editIcon}
                      title="Editar ponto"
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
                      placeholder="Marca"
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
                      Disponível
                    </label>
                    <div className={styles.formActions}>
                      <button onClick={handleSubmit}>Salvar</button>
                      <button onClick={() => setShowFormSlotIndex(null)}>Cancelar</button>
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
          <button className={styles.cancelBtn} onClick={onClose}>Fechar</button>
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
