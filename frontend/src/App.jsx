import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import RegisterPage from "./pages/RegisterPage";
import { UserProvider } from "./context/UserContext";
import VehiclesPage from "./pages/VehiclesPage";
import ReservationsPage from "./pages/ReservationsPage";
import SlotPage from "./pages/SlotPage";
import { Elements } from '@stripe/react-stripe-js';
import { loadStripe } from '@stripe/stripe-js';
import PaymentSuccessPage from "./pages/PaymentSuccessPage";

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

      </Routes>
    </Router>
    </UserProvider>

    </Elements>
  );
}
