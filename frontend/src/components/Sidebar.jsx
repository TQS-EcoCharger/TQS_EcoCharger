import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import styles from '../css/Sidebar.module.css';
import { useUser } from "../context/UserContext";


export default function Sidebar() {
  const { userType } = useUser();
  return (
    <div style={{ display: 'flex', height: '100vh', overflow: 'hidden' }} id="layout-container">
      <div className={styles.sidebar} id="sidebar">
        <img
          src="/logo.png"
          alt="Perfil"
          className={styles.profileImage}
          id="sidebar-logo"
        />
        <ul className={styles.menu} id="sidebar-menu">
          {userType === 'administrator' && (
            <li><NavLink to="/home">Dashboard</NavLink></li>
          )}
          <li><NavLink to="/home" id="nav-home">Map</NavLink></li>
          <li><NavLink to="/stations" id="nav-stations">Charging Stations</NavLink></li>
          <li><NavLink to="/reservations" id="nav-reservations">Reservations</NavLink></li>
          <li><NavLink to="/vehicles" id="nav-vehicles">Vehicles</NavLink></li>

          <ul className={styles.bottomMenu}>
          <li><NavLink to="/profile" id="nav-profile">Profile</NavLink></li>
          <li><NavLink to="/logout">Logout</NavLink></li>
        </ul>
        </ul>
      </div>
      <main style={{ flex: 1, overflow: 'auto' }} id="main-content">
        <Outlet />
      </main>
    </div>
  );
}