import React from "react";
import styles from "../css/LoginPage.module.css";
import { motion } from "framer-motion";
import logo from "../assets/logo.png";

const LoginPage = () => {
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
          <form className={styles.form}>
            <div className={styles.inputGroup}>
              <label htmlFor="email" className={styles.label}>
                Email
              </label>
              <input
                type="email"
                id="email"
                placeholder="you@example.com"
                className={styles.input}
              />
            </div>
            <div className={styles.inputGroup}>
              <label htmlFor="password" className={styles.label}>
                Password
              </label>
              <input
                type="password"
                id="password"
                placeholder="••••••••"
                className={styles.input}
              />
            </div>
            <button type="submit" className={styles.button}>
              Login
            </button>
          </form>
        </div>
      </motion.div>
    </div>
  );
};

export default LoginPage;
