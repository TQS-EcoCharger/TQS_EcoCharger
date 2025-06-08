import React, { useState, useEffect } from "react";
import styles from "../css/VehicleDetails.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faGauge,
  faCalendarAlt,
  faBolt,
  faCar,
  faPenToSquare
} from "@fortawesome/free-solid-svg-icons";
import AddCarModal from "../components/AddCarModal";

export default function VehicleDetails({ vehicle, onEdit }) {
  const [initialVehicle, setInitialVehicle] = useState(vehicle);
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    setInitialVehicle(vehicle);
  }, [vehicle]);

  if (!vehicle) {
    return (
      <div id="vehicle-details-loading" className={styles.loading}>
        Loading vehicle details...
      </div>
    );
  }

  const handleCloseModal = () => {
    setModalOpen(false);
  };

  const onEditVehicle = (updatedVehicle) => {
    onEdit(initialVehicle.id, updatedVehicle);
    setModalOpen(false);
  };



  return (
    <div id="vehicle-details-container" className={styles.detailsContainer}>
      <div id="vehicle-name" className={styles.vehicleName}>
        {initialVehicle.name}
        <FontAwesomeIcon
          icon={faPenToSquare}
          id="edit-vehicle-button"
          className={styles.editButton}
          onClick={() => {
            setModalOpen(true);
          }}
        />
      </div>
      <div id="vehicle-make-model" className={styles.vehicleInfo}>
        {initialVehicle.make} • {initialVehicle.model}
      </div>

      <div id="vehicle-cards-grid" className={styles.cardGrid}>
        <div id="vehicle-mileage-card" className={styles.card}>
          <FontAwesomeIcon icon={faGauge} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>Mileage</h3>
            <p id="vehicle-mileage-value">{initialVehicle.kilometers} km</p>
          </div>
        </div>

        <div id="vehicle-registered-card" className={styles.card}>
          <FontAwesomeIcon icon={faCalendarAlt} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>Registered</h3>
            <p id="vehicle-year-value">{initialVehicle.year || "Unknown"}</p>
          </div>
        </div>

        <div id="vehicle-consumption-card" className={styles.card}>
          <FontAwesomeIcon icon={faBolt} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>Energy Consumption</h3>
            <p id="vehicle-consumption-value">
              {initialVehicle.consumption} kWh
            </p>
          </div>
        </div>

        <div id="vehicle-battery-card" className={styles.card}>
          <FontAwesomeIcon icon={faBolt} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>Battery Capacity</h3>
            <p id="vehicle-battery-value">
              {initialVehicle.batteryCapacity} kWh
            </p>
          </div>
        </div>

        <div id="vehicle-license-card" className={styles.card}>
          <FontAwesomeIcon icon={faCar} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>License Plate</h3>
            <p id="vehicle-license-value">{initialVehicle.licensePlate}</p>
          </div>
        </div>
      </div>

      <AddCarModal
        isOpen={modalOpen}
        onClose={handleCloseModal}
        onAdd={onEditVehicle}
        existingData={initialVehicle}
      />
    </div>
  );
}
