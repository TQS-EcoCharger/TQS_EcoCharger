import React, { useEffect, useState } from "react";
import Table from "../components/Table";
import Sidebar from "../components/Sidebar";
import styles from "../css/VehiclesPage.module.css";
import AddCarModal from "../components/AddCarModal";
import axios from "axios";
import CONFIG from "../../config";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faTrash } from "@fortawesome/free-solid-svg-icons";
import VehicleDetails from "./VehicleDetails";

export default function VehiclesPage() {
  const [cars, setCars] = useState([]);
  const [currentCar, setCurrentCar] = useState(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");  // NEW

  const fetchVehicles = async () => {
    const meId = localStorage.getItem("me") ? JSON.parse(localStorage.getItem("me")).id : null;

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
      fetchVehicles();
    };

    fetchSelf();
  }, []);

  const showTemporarySuccess = (message) => {
    setSuccessMessage(message);
    setTimeout(() => setSuccessMessage(""), 3000); // Hide after 3 seconds
  };

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
      showTemporarySuccess("Vehicle added successfully!");
    } catch (error) {
      console.error("Error adding car:", error);
      alert("Failed to add car: " + (error.response?.data || error.message));
    }
  };

  const handleEditCar = async (carId, updatedData) => {
    const meId = localStorage.getItem("me") ? JSON.parse(localStorage.getItem("me")).id : null;
    console.log("Editing car with ID:", carId, "and data:", updatedData);
    if (!meId) return;
    try {
      await axios.patch(`${CONFIG.API_URL}v1/driver/${meId}/cars/${carId}`, updatedData, {
        headers: {
          "Authorization": "Bearer " + localStorage.getItem("token"),
          "Content-Type": "application/json",
        },
      });
      if (currentCar && currentCar.id === carId) {
        setCurrentCar(prev => ({ ...prev, ...updatedData }));
      }
      fetchVehicles();
      showTemporarySuccess("Vehicle updated successfully!");
    }
    catch (error) {
      console.error("Error editing car:", error);
      alert("Failed to edit car: " + (error.response?.data || error.message));
    }
  };

  const handleDelete = async (carId) => {
    const meId = localStorage.getItem("me") ? JSON.parse(localStorage.getItem("me")).id : null;
    if (!meId) return;

    try {
      await axios.delete(`${CONFIG.API_URL}v1/driver/${meId}/cars/${carId}`, {
        headers: {
          "Authorization": "Bearer " + localStorage.getItem("token"),
        },
      });
      showTemporarySuccess("Vehicle deleted successfully!");
      fetchVehicles();
    } catch (error) {
      console.error("Error deleting car:", error);
      alert("Failed to delete car: " + (error.response?.data || error.message));
    }
  };

  const openVehicleDetails = (car) => {
    setCurrentCar(car);
  };

  const goBackToList = () => {
    setCurrentCar(null);
  };

  return (
    <div className={styles.pageContainer} id="vehicles-page">
      <Sidebar />
      <div className={styles.content}>
        {currentCar ? (
          <div id="vehicle-details-container">
            <button
              className={styles.addCarButton}
              onClick={goBackToList}
              id="vehicle-back-button"
            >
              ← Back to List
            </button>
            <VehicleDetails vehicle={currentCar} onEdit={handleEditCar} />
          </div>
        ) : (
          <>
            <h1 id="vehicles-title">Vehicles Page</h1>
            <button
              className={styles.addCarButton}
              onClick={() => setModalOpen(true)}
              id="add-car-button"
            >
              Add New Car
            </button>
            <Table
              id="vehicle-table"
              headers={["ID", "Name", "Make", "Actions"]}
              rows={cars.map(car => [
                car.id,
                car.name,
                car.make,
                <div
                  className={styles.actionButtons}
                  id={`car_actions_${car.id}`}
                >
                  <button
                    className={styles.iconButton}
                    onClick={() => openVehicleDetails(car)}
                    title="View Details"
                    data-vehicle-name={car.name}
                    id={`view_car_${car.id}`}
                  >
                    <FontAwesomeIcon icon={faEye} />
                  </button>
                  <button
                    className={styles.iconButton}
                    onClick={() => handleDelete(car.id)}
                    title="Delete Car"
                    id={`delete_car_${car.id}`}
                  >
                    <FontAwesomeIcon icon={faTrash} />
                  </button>
                </div>
              ])}
            />

            <AddCarModal
              isOpen={modalOpen}
              onClose={() => setModalOpen(false)}
              onAdd={handleAddCar}
              id="add-car-modal"
            />
            {/* Success message shown here after modal closes */}
            {successMessage && (
              <div className={styles.successMessage} id="success-message">
                {successMessage}
              </div>
            )}

          </>
        )}
      </div>
    </div>
  );
}
