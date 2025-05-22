import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import styles from '../css/Sidebar.module.css';

export default function Sidebar() {
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
          <li><NavLink to="/home">Mapa</NavLink></li>
          <li><NavLink to="/stations">Estações</NavLink></li>
          <li><NavLink to="/profile">Perfil</NavLink></li>
        </ul>
      </div>
      <main style={{ flex: 1, overflow: 'auto' }}>
        <Outlet />
      </main>
    </div>
  );
}
