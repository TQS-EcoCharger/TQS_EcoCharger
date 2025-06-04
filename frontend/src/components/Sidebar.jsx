import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import styles from '../css/Sidebar.module.css';

export default function Sidebar() {
  return (
    <div style={{ display: 'flex', height: '100vh', overflow: 'hidden' }} id="layout-container">
      <div className={styles.sidebar} id="sidebar">
        <img
          src="/logo.png"
          alt="Perfil"
          className={styles.profileImage}
          id="sidebar-logo"
        />
        <div className={styles.title} id="sidebar-title">EcoCharger</div>
        <ul className={styles.menu} id="sidebar-menu">
          <li><NavLink to="/home" id="nav-home">Mapa</NavLink></li>
          <li><NavLink to="/stations" id="nav-stations">Estações</NavLink></li>
          <li><NavLink to="/profile" id="nav-profile">Perfil</NavLink></li>
          <li><NavLink to="/reservations" id="nav-reservations">Reservas</NavLink></li>
        </ul>
      </div>
      <main style={{ flex: 1, overflow: 'auto' }} id="main-content">
        <Outlet />
      </main>
    </div>
  );
}