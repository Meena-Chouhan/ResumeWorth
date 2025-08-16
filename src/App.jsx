import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import ResumeUpload from "./pages/ResumeUpload";
import LinkedInSubmit from "./pages/LinkedInSubmit";
import Navbar from "./components/Navbar";
import PrivateRoute from "./components/PrivateRoute";
import "./App.css";

export default function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        {/* Public routes */}
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Protected routes */}
        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/resume-upload"
          element={
            <PrivateRoute>
              <ResumeUpload />
            </PrivateRoute>
          }
        />
        <Route
          path="/linkedin-upload"
          element={
            <PrivateRoute>
              <LinkedInSubmit />
            </PrivateRoute>
          }
        />

      </Routes>
    </Router>
  );
}
