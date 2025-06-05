import React, { useState } from "react";
import axios from "axios";
import styles from "../css/LoginPage.module.css";
import { motion } from "framer-motion";
import logo from "../assets/logo.png";
import CONFIG from "../../config";
import { useNavigate } from "react-router-dom";
import { useUser } from "../context/UserProvider";

const LoginPage = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useUser();
  const navigate = useNavigate();
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
     const response = await axios.post(`${CONFIG.API_URL}auth/login`, {
        email,
        password,
      });

      console.log("Login successful:", response.data);
      login(response.data.token, response.data.userType);
      navigate("/home");
    

    } catch (err) {
      console.error("Login failed:", err.response?.data || err.message);
      setError(err.response?.data || "Login failed. Please try again.");
    }
  };

  return (
    <div className={styles.container}>
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 0.6, ease: "easeOut" }}
        className={styles.cardWrapper}
      >
        <div className={styles.card}>
          <motion.img
            src={logo}
            alt="Logo"
            className={styles.logo}
            initial={{ opacity: 0, y: -20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.5, delay: 0.3 }}
          />
          <h2 className={styles.title}>Welcome Back</h2>
            <form className={styles.form} onSubmit={handleSubmit} id="login-form">
              <div className={styles.inputGroup}>
                <label htmlFor="email" className={styles.label}>Email</label>
                <input
                  type="email"
                  id="email"
                  placeholder="you@example.com"
                  className={styles.input}
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className={styles.inputGroup}>
                <label htmlFor="password" className={styles.label}>Password</label>
                <input
                  type="password"
                  id="password"
                  placeholder="••••••••"
                  className={styles.input}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              {error && <p className={styles.error} id="login-error">{error}</p>}
              <button type="submit" className={styles.button} id="login-button">
                Login
              </button>
              <p className={styles.footerText}>
                Don't have an account?{" "}
                <a href="/register" className={styles.link}>
                  Register
                </a>
              </p>
            </form>
        </div>
      </motion.div>
    </div>
  );
};

export default LoginPage;
