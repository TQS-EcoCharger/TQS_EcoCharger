import React, { createContext, useContext, useState } from "react";

const UserContext = createContext();

export const UserProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [id, setId] = useState(localStorage.getItem("id") || null);
  const [userType, setUserType] = useState(localStorage.getItem("userType") || null);

  const login = (newToken, newUserType, id) => {
    setToken(newToken);
    setUserType(newUserType);
    setId(id);
    localStorage.setItem("token", newToken);
    localStorage.setItem("id", id);
    localStorage.setItem("userType", newUserType);
  };

  const logout = () => {
    setToken(null);
    setUserType(null);
    localStorage.removeItem("token");
    localStorage.removeItem("userType");
  };

  return (
    <UserContext.Provider value={{ token, userType, id, login, logout }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => useContext(UserContext);
