import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import { UserProvider } from "./context/UserContext";
import VehiclesPage from "./pages/VehiclesPage";
import ReservationsPage from "./pages/ReservationsPage";
import StatisticsPage from "./pages/StatisticsPage";

export default function App() {
  return (
    <UserProvider>
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/vehicles" element={<VehiclesPage />} />
        <Route path="/reservations" element={<ReservationsPage />} />
        <Route path="/statistics" element={<StatisticsPage />} />
      </Routes>
    </Router>
    </UserProvider>
  );
}
