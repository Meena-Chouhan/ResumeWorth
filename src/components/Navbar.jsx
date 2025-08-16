import React from "react";
import { NavLink } from "react-router-dom";
import "./Navbar.css";

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="logo">ResumeWorth</div>
      <div className="nav-links">
        <NavLink to="/dashboard" activeclassname="active">Dashboard</NavLink>
        <NavLink to="/resume-upload" activeclassname="active">Resume</NavLink>
        <NavLink to="/linkedin-upload" activeclassname="active">LinkedIn</NavLink>
        <NavLink to="/login" activeclassname="active">Login</NavLink>
        <NavLink to="/register" activeclassname="active">Register</NavLink>
      </div>
    </nav>
  );
};

export default Navbar;
