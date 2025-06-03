import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import styles from '../css/Sidebar.module.css';
import { useUser } from "../context/UserContext";


export default function Sidebar() {
  const { userType } = useUser();
  return (
    <div style={{ display: 'flex', height: '100vh', overflow: 'hidden' }}>
      <div className={styles.sidebar}>
        <img
          src="/logo.png"
          alt="Perfil"
          className={styles.profileImage}
        />
        <div className={styles.title}>EcoCharger</div>
        <ul className={styles.menu}>
          { userType === 'administrator' && (
          <li><NavLink to="/home">Mapa</NavLink></li>
          )}
          <li><NavLink to="/stations">Estações</NavLink></li>
          <li><NavLink to="/profile">Perfil</NavLink></li>
          <li><NavLink to="/reservations">Reservas</NavLink></li>
        </ul>
      </div>
      <main style={{ flex: 1, overflow: 'auto' }}>
        <Outlet />
      </main>
    </div>
  );
}
