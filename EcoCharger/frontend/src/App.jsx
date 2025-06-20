import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import VehiclesPage from "./pages/VehiclesPage";
import ReservationsPage from "./pages/ReservationsPage";
import { UserProvider } from "./context/UserProvider";
import SlotPage from "./pages/SlotPage";
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import PaymentSuccessPage from "./pages/PaymentSuccessPage";
import StatisticsPage from "./pages/StatisticsPage";
import StatsDriver from "./pages/StatsDriver";

const stripePromise = loadStripe('pk_test_51RWoucBUaeM9jaPnE7P8D5GCtCYPFiWiKUSNkhO7gCGgvMP2S6bYAEdhcURbotn8MzbvRatJZREJGWXClhNilImr001u8jYFwK');


export default function App() {
  return (
    <Elements stripe={stripePromise}>

    <UserProvider>
    <Router>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/home" element={<HomePage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/vehicles" element={<VehiclesPage />} />
        <Route path="/reservations" element={<ReservationsPage />} />
        <Route path="/slots/:id" element={<SlotPage />} />
        <Route path="/payment-success" element={<PaymentSuccessPage />} />
        <Route path="/statistics" element={<StatisticsPage />} />
        <Route path="/statsDriver" element={<StatsDriver />} />
      </Routes>
    </Router>
    </UserProvider>

    </Elements>
  );
}
