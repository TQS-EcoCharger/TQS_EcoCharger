import React, { useEffect, useState } from 'react';
import { NavLink, Outlet } from 'react-router-dom';
import styles from '../css/Sidebar.module.css';
import axios from 'axios';
import CONFIG from '../config';
import Modal from 'react-modal';
import { useNavigate } from 'react-router-dom';

Modal.setAppElement('#root');
import { useUser } from '../context/UserProvider.jsx';

export default function Sidebar() {
  const { userType, token } = useUser();
  const me = JSON.parse(localStorage.getItem('me'));
  const userId = me ? me.id : null;
  const [balance, setBalance] = useState(null);
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [amount, setAmount] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchBalance = async () => {
      if (userType === 'driver') {
        try {
          const res = await axios.get(`${CONFIG.API_URL}v1/driver/${userId}`, {
            headers: { Authorization: `Bearer ${token}` }
          });
          setBalance(res.data.balance ?? 0);
        } catch (err) {
          console.error('Failed to fetch balance:', err);
        }
      }
    };

    fetchBalance();
  }, [userType, userId, token]);

    const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('me');
    navigate('/'); 
  };

  const handleTopUp = async () => {
    try {
      const res = await axios.post(
        `${CONFIG.API_URL}v1/driver/${userId}/balance`,
        {
          amount: parseFloat(amount),
          simulateSuccess: false, 
        },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (res.data.simulated) {
        setModalIsOpen(false);
        setAmount('');
        const balRes = await axios.get(`${CONFIG.API_URL}v1/driver/${userId}`, {
          headers: { Authorization: `Bearer ${token}` }
        });
        setBalance(balRes.data.balance ?? 0);
      } else if (res.data.url) {
        window.location.href = res.data.url;
      }

    } catch (err) {
      console.error('Top-up failed:', err);
      alert('Failed to initiate payment');
    }
  };

  return (
    <div style={{ display: 'flex', height: '100vh' }} id="layout-container">
      <div className={styles.sidebar} id="sidebar">
        <img src="/logo.png" alt="Logo" className={styles.profileImage} id="sidebar-logo" />

        <ul className={styles.menu} id="sidebar-menu">
          {userType === 'administrator' ? (
            <>
              <li><NavLink to="/home" id="nav-dashboard">Dashboard</NavLink></li>
              <li><NavLink to="/statistics" id="nav-statistics">Statistics</NavLink></li>
            </>
          ) : (
            <li><NavLink to="/home" id="nav-home">Map</NavLink></li>
          )}
          <li><NavLink to="/reservations" id="nav-reservations">Reservations</NavLink></li>
          <li><NavLink to="/vehicles" id="nav-vehicles">Vehicles</NavLink></li>
        </ul>

        <ul className={styles.bottomMenu}>
          <li><NavLink to="/profile" id="nav-profile">Profile</NavLink></li>
          <li>
            <button
              onClick={handleLogout}
              className={styles.logoutBtn}
              id="nav-logout"
            >
              Logout
            </button>
          </li>
          {userType === 'driver' && (
            <>
              <li style={{ marginTop: '1rem', color: '#f4cc5d', fontWeight: 'bold' }}>
                Balance: €{balance !== null ? balance.toFixed(2) : '...'}
              </li>
              <li>
                <button
                  onClick={() => setModalIsOpen(true)}
                  className={styles.balanceBtn}
                  style={{
                    background: '#f4cc5d',
                    color: '#000',
                    fontWeight: 'bold',
                    border: 'none',
                    padding: '6px 12px',
                    borderRadius: '6px',
                    marginTop: '0.5rem'
                  }}
                >
                  Charge Balance
                </button>
              </li>
            </>
          )}
        </ul>
      </div>

      <main style={{ flex: 1, overflow: 'auto' }} id="main-content">
        <Outlet />
      </main>

      <Modal
        isOpen={modalIsOpen}
        onRequestClose={() => setModalIsOpen(false)}
        contentLabel="Add Balance"
        style={{
          content: {
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            backgroundColor: '#1e1e1e',
            padding: '2rem',
            borderRadius: '12px',
            color: 'white',
            width: '400px',
            height: '250px',
          },
        }}
      >
        <h2 style={{ color: '#f4cc5d' }}>Add Balance</h2>

        <input
          type="number"
          placeholder="Amount (€)"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
          style={{
            margin: '1rem 0',
            padding: '8px',
            width: '100%',
            borderRadius: '8px',
            border: '1px solid #ccc',
          }}
        />
        <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem', marginTop: '1rem' }}>
          <button
            onClick={handleTopUp}
            style={{
              background: '#f4cc5d',
              color: '#000',
              fontWeight: 'bold',
              padding: '10px 16px',
              borderRadius: '6px',
              border: 'none',
              width: '100%',
            }}
            id="top-up-button"
          >
            Proceed to Payment
          </button>
          <button
            onClick={() => setModalIsOpen(false)}
            style={{
              background: 'transparent',
              border: '1px solid #ccc',
              color: 'white',
              padding: '10px 16px',
              borderRadius: '6px',
              width: '100%',
            }}
          >
            Cancel
          </button>
        </div>
      </Modal>
    </div>
  );
}
