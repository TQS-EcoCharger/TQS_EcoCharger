import React from "react";
import styles from "../css/VehicleDetails.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faGauge,
  faCalendarAlt,
  faBolt,
  faCar,
  faPenToSquare
} from "@fortawesome/free-solid-svg-icons";

export default function VehicleDetails({ vehicle }) {
  if (!vehicle) {
    return <div className={styles.loading}>Loading vehicle details...</div>;
  }

  return (
    <div className={styles.detailsContainer}>
      <h1 className={styles.vehicleName}>{vehicle.name}</h1>
      <FontAwesomeIcon icon={faPenToSquare} className={styles.editButton}/>
      <h2 className={styles.vehicleInfo}>{vehicle.make} • {vehicle.model}</h2>

      
      <div className={styles.cardGrid}>
        <div className={styles.card}>
          <FontAwesomeIcon icon={faGauge} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>Mileage</h3>
            <p>{vehicle.kilometers} km</p>
          </div>
        </div>

        <div className={styles.card}>
          <FontAwesomeIcon icon={faCalendarAlt} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>Registered</h3>
            <p>{vehicle.year || "Unknown"}</p>
          </div>
        </div>

        <div className={styles.card}>
          <FontAwesomeIcon icon={faBolt} className={styles.icon} />
          <div className={styles.cardContent}>
            <h3>Energy Consumption</h3>
            <p>{vehicle.consumption} kWh</p>
          </div>
        </div>

        <div className={styles.card}>
            <FontAwesomeIcon icon={faBolt} className={styles.icon} />
            <div className={styles.cardContent}>
                <h3>Battery Capacity</h3>
                <p>{vehicle.batteryCapacity} kWh</p>
            </div>
        </div>

        <div className={styles.card}>
            <FontAwesomeIcon icon={faCar} className={styles.icon} />
            <div className={styles.cardContent}>
                <h3>License Plate</h3>
                <p>{vehicle.licensePlate}</p>
            </div>
        </div>
      </div>
    </div>
  );
}
