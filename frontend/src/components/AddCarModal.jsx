import React, { useState } from "react";
import styles from "../css/AddCarModal.module.css";

export default function AddCarModal({ isOpen, onClose, onAdd }) {
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
    <div className={styles.modalOverlay}>
      <div className={styles.modal}>
        <h2>Add New Car</h2>
        <form className={styles.form} onSubmit={handleSubmit}>
          <label>Name:
            <input name="name" value={form.name} onChange={handleChange} required />
          </label>
          <label>Make:
            <input name="make" value={form.make} onChange={handleChange} required />
          </label>
          <label>Model:
            <input name="model" value={form.model} onChange={handleChange} required />
          </label>
          <label>Year:
            <input type="number" name="year" value={form.year} onChange={handleChange} required min="1900" max={new Date().getFullYear()} />
          </label>
          <label>License Plate:
            <input name="licensePlate" value={form.licensePlate} onChange={handleChange} required />
          </label>
          <label>Battery Capacity (kWh):
            <input type="number" step="0.1" name="batteryCapacity" value={form.batteryCapacity} onChange={handleChange} required min="0" />
          </label>
          <label>Battery Level (kWh):
            <input type="number" step="0.1" name="batteryLevel" value={form.batteryLevel} onChange={handleChange} required min="0" />
          </label>
          <label>Kilometers:
            <input type="number" step="0.1" name="kilometers" value={form.kilometers} onChange={handleChange} required min="0" />
          </label>
          <label>Consumption (kWh/100km):
            <input type="number" step="0.1" name="consumption" value={form.consumption} onChange={handleChange} required min="0" />
          </label>
          <div className={styles.buttons}>
            <button type="submit">Add Car</button>
            <button type="button" onClick={onClose} className={styles.cancelBtn}>Cancel</button>
          </div>
        </form>
      </div>
    </div>
  );
}
