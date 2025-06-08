import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import CONFIG from '../config';

export default function PaymentSuccessPage() {
  const navigate = useNavigate();

  useEffect(() => {
    const finalizePayment = async () => {
      const params = new URLSearchParams(window.location.search);
      const sessionId = params.get('session_id');

      const token = localStorage.getItem('token'); 
      if (!sessionId || !token) {
        alert('Missing session ID or token');
        navigate('/home');
        return;
      }

      try {
        const res = await axios.get(`${CONFIG.API_URL}v1/driver/checkout-success`, {
          params: { session_id: sessionId },
          headers: {
            Authorization: `Bearer ${token}` 
          }
        });

        console.log('Payment success:', res.data);
        navigate('/home');
      } catch (err) {
        console.error('Failed to finalize payment:', err);
        navigate('/home');
      }
    };

    finalizePayment();
  }, [navigate]);

  return (
    <div style={{ textAlign: 'center', marginTop: '5rem' }}>
      <h2 style={{ color: 'white' }}>Processing payment...</h2>
    </div>
  );
}
