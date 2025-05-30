/* Done with the help of ChatGPT */

import React, { useState } from "react";
import axios from "axios";
import styles from "../css/LoginPage.module.css";
import { motion } from "framer-motion";
import logo from "../assets/logo.png";
import CONFIG from "../../config";
import { useNavigate, Link } from "react-router-dom";

const RegisterPage = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [isEmailValid, setIsEmailValid] = useState(true);
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const validateEmail = (value) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    setIsEmailValid(emailRegex.test(value));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    if (!isEmailValid) {
      setError("Please enter a valid email.");
      return;
    }

    try {
      const response = await axios.post(`${CONFIG.API_URL}auth/register`, {
        name,
        email,
        password,
      });

      console.log("Registration successful:", response.data);
      localStorage.setItem("token", response.data.token);
      navigate("/home");
    } catch (err) {
      console.error("Registration failed:", err.response?.data || err.message);
      setError(err.response?.data || "Registration failed. Please try again.");
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
          <h2 className={styles.title}>Create an Account</h2>
          <form className={styles.form} onSubmit={handleSubmit} id="register-form">
            <div className={styles.inputGroup}>
              <label htmlFor="name" className={styles.label}>Name</label>
              <input
                type="text"
                id="name"
                placeholder="John Doe"
                className={styles.input}
                value={name}
                onChange={(e) => setName(e.target.value)}
                required
              />
            </div>
            <div className={styles.inputGroup}>
              <label htmlFor="email" className={styles.label}>Email</label>
              <input
                type="email"
                id="email"
                placeholder="you@example.com"
                className={`${styles.input} ${!isEmailValid ? styles.invalid : ""}`}
                value={email}
                onChange={(e) => {
                  setEmail(e.target.value);
                  validateEmail(e.target.value);
                }}
                required
              />
              {!isEmailValid && (
                <small className={styles.error} id="invalid-email">Invalid email format</small>
              )}
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
            <div className={styles.inputGroup}>
              <label htmlFor="confirmPassword" className={styles.label}>Confirm Password</label>
              <input
                type="password"
                id="confirmPassword"
                placeholder="••••••••"
                className={styles.input}
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
              />
            </div>
            {error && <p className={styles.error} id="register-error">{error}</p>}
            <button type="submit" className={styles.button} id="register-button">
              Register
            </button>
            <p className={styles.footerText}>
              Already have an account?{" "}
              <Link to="/" className={styles.link}>
                Login
              </Link>
            </p>
          </form>
        </div>
      </motion.div>
    </div>
  );
};

export default RegisterPage;
