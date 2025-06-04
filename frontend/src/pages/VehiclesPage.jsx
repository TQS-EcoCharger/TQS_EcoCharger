import React, { useEffect, useState } from "react";
import Table from "../components/Table";
import Sidebar from "../components/Sidebar";
import styles from "../css/VehiclesPage.module.css";
import AddCarModal from "../components/AddCarModal";
import axios from "axios";
import CONFIG from "../../config";

export default function VehiclesPage() {
  const [cars, setCars] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);

  const fetchVehicles = async () => {
    const meId = localStorage.getItem("me") ? JSON.parse(localStorage.getItem("me")).id : null;
    if (!meId) return;

    try {
      const response = await axios.get(`${CONFIG.API_URL}v1/driver/${meId}`, {
        headers: {
          "Authorization": "Bearer " + localStorage.getItem("token")
        }
      });
      console.log("Vehicles fetched successfully:", response.data.cars);
      setCars(response.data.cars || []);
    } catch (error) {
      console.error("Error fetching vehicles:", error);
    }
  };

  useEffect(() => {
    const fetchSelf = async () => {
      if (localStorage.getItem("me") === null) {
        try {
          const response = await axios.get(`${CONFIG.API_URL}auth/me`, {
            headers: {
              "Authorization": localStorage.getItem("token")
            }
          });
          console.log("Self data fetched successfully:", response.data);
          localStorage.setItem("me", JSON.stringify(response.data));
        } catch (error) {
          console.error("Error fetching self data:", error);
        }
      } else {
        console.log("Self data already exists in localStorage.");
      }
    };

    fetchSelf();
    fetchVehicles();
  }, []);

  const handleAddCar = async (carData) => {
    const meId = localStorage.getItem("me") ? JSON.parse(localStorage.getItem("me")).id : null;
    if (!meId) return;

    try {
      await axios.patch(`${CONFIG.API_URL}v1/driver/${meId}/cars/`, carData, {
        headers: {
          "Authorization": "Bearer " + localStorage.getItem("token"),
          "Content-Type": "application/json",
        },
      });
      setModalOpen(false);
      fetchVehicles();
    } catch (error) {
      console.error("Error adding car:", error);
      alert("Failed to add car: " + (error.response?.data || error.message));
    }
  };

  return (
    <div className={styles.pageContainer}>
      <Sidebar />
      <div className={styles.content}>
        <h1>Vehicles Page</h1>
        <button className={styles.addCarButton} onClick={() => setModalOpen(true)}>Add New Car</button>
        <Table
          headers={["ID", "Model", "Year"]}
          rows={cars.map(car => [
            car.id,
            car.model,
            car.year
          ])}
        />
        <AddCarModal
          isOpen={modalOpen}
          onClose={() => setModalOpen(false)}
          onAdd={handleAddCar}
        />
        <p>More content can be added here.</p>
      </div>
    </div>
  );
}
