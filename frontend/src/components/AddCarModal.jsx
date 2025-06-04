import React, { useState, useEffect } from "react";
import styles from "../css/AddCarModal.module.css";

export default function AddCarModal({ isOpen, onClose, onAdd, existingData }) {
  const [form, setForm] = useState({
    name: "",
    make: "",
    model: "",
    year: new Date().getFullYear(),
    licensePlate: "",
    batteryCapacity: "",
    batteryLevel: "",
    kilometers: "",
    consumption: ""
  });
  useEffect(() => {
    if (existingData && isOpen) {
      setForm({
        name: existingData.name || "",
        make: existingData.make || "",
        model: existingData.model || "",
        year: existingData.year || new Date().getFullYear(),
        licensePlate: existingData.licensePlate || "",
        batteryCapacity: existingData.batteryCapacity || "",
        batteryLevel: existingData.batteryLevel || "",
        kilometers: existingData.kilometers || "",
        consumption: existingData.consumption || ""
      });
    } else if (isOpen) {
      // Reset form if adding a new car
      setForm({
        name: "",
        make: "",
        model: "",
        year: new Date().getFullYear(),
        licensePlate: "",
        batteryCapacity: "",
        batteryLevel: "",
        kilometers: "",
        consumption: ""
      });
    }
  }, [existingData, isOpen]);


  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = e => {
    e.preventDefault();

    // Basic validation could be added here if needed
    onAdd({
      ...form,
      year: Number(form.year),
      batteryCapacity: Number(form.batteryCapacity),
      batteryLevel: Number(form.batteryLevel),
      kilometers: Number(form.kilometers),
      consumption: Number(form.consumption)
    });
  };

  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay} id="add-car-modal-overlay">
      <div className={styles.modal} id="add-car-modal">
        <h2 id="action-performed">{existingData ? 'Edit Car' : 'Add New Car'}</h2>
        <form className={styles.form} onSubmit={handleSubmit} id="add-car-form">
          <label id="name-label">Name:
            <input name="name" value={form.name} onChange={handleChange} required id="name-input"/>
          </label>
          <label id="make-label">Make:
            <input name="make" value={form.make} onChange={handleChange} required id="make-input"/>
          </label>
          <label id="model-label">Model:
            <input name="model" value={form.model} onChange={handleChange} required id="model-input"/>
          </label>
          <label id="year-label">Year:
            <input type="number" name="year" value={form.year} onChange={handleChange} required min="1900" max={new Date().getFullYear()} id="year-input" />
          </label>
          <label id="license-label">License Plate:
            <input name="licensePlate" value={form.licensePlate} onChange={handleChange} required id="license-input"/>
          </label>
          <label id="capacity-label">Battery Capacity (kWh):
            <input type="number" step="0.1" name="batteryCapacity" value={form.batteryCapacity} onChange={handleChange} required min="0" id="capacity-input"/>
          </label>
          <label id="level-label">Battery Level (kWh):
            <input type="number" step="0.1" name="batteryLevel" value={form.batteryLevel} onChange={handleChange} required min="0" id="level-input"/>
          </label>
          <label id="kilometers-label">Kilometers:
            <input type="number" step="0.1" name="kilometers" value={form.kilometers} onChange={handleChange} required min="0" id="kilometers-input"/>
          </label>
          <label id="consumption-label">Consumption (kWh/100km):
            <input type="number" step="0.1" name="consumption" value={form.consumption} onChange={handleChange} required min="0" id="consumption-input"/>
          </label>
          <div className={styles.buttons} id="add-car-buttons">
            <button type="submit" className={styles.submitBtn} id="submit-button">{existingData ? 'Edit Car' : 'Add Car'}</button>
            <button type="button" onClick={onClose} className={styles.cancelBtn} id="cancel-button">Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
}
