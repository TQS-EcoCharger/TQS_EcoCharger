import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import VehiclesPage from "./pages/VehiclesPage";
import ReservationsPage from "./pages/ReservationsPage";
<<<<<<< HEAD
import StatisticsPage from "./pages/StatisticsPage";
=======
import { UserProvider } from "./context/UserProvider";
import SlotPage from "./pages/SlotPage";
>>>>>>> develop

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
<<<<<<< HEAD
        <Route path="/statistics" element={<StatisticsPage />} />
=======
        <Route path="/slots/:id" element={<SlotPage />} />
>>>>>>> develop
      </Routes>
    </Router>
    </UserProvider>
  );
}
