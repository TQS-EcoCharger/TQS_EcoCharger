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
        <ul className={styles.menu}>
          {userType === 'administrator' && (
            <li><NavLink to="/home">Dashboard</NavLink></li>
          )}
          <li><NavLink to="/home">Map</NavLink></li>
          <li><NavLink to="/stations">Charging Stations</NavLink></li>
        </ul>
        <ul className={styles.bottomMenu}>
          <li><NavLink to="/profile">Profile</NavLink></li>
          <li><NavLink to="/logout">Logout</NavLink></li>
        </ul>
      </div>
      <main style={{ flex: 1, overflow: 'auto' }}>
        <Outlet />
      </main>
    </div>
  );
}
