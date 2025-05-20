import React from "react";

const HomePage = () => {
  return (
    <div id="home-page-content" style={styles.container}>
      <h1>Welcome to EcoCharger</h1>
      <p>You have successfully logged in or registered.</p>
    </div>
  );
};

const styles = {
  container: {
    padding: "2rem",
    textAlign: "center",
    fontFamily: "Arial, sans-serif",
  },
};

export default HomePage;
