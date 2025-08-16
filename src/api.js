// src/services/api.js

import axios from "axios";

// Base URL of your backend
const API = axios.create({
  baseURL: "http://localhost:8082", // ✅ change this if deployed
  headers: {
    "Content-Type": "application/json",
  },
});

// Add Authorization token automatically if present
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ========== API ENDPOINTS ==========

// User auth
export const loginUser = (credentials) => API.post("/auth/login", credentials);
export const registerUser = (userData) => API.post("/auth/register", userData);

// Dashboard
export const fetchDashboard = (userId) =>
  API.get(`/user/DashBoard/${userId}`);

export const fetchUserInfo = (userId) =>
  API.get(`/user/myInfo/${userId}`);

// Resume Upload (multipart/form-data)
export const uploadResume = (formData) =>
  API.post("/api/resume/upload", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

// LinkedIn URL Submission
export const uploadLinkedIn = (data) => API.post("/api/linkedin/analyze", data);

// Get previous resume analyses
export const getAllResumeSummaries = () =>
  API.get("/api/resume/allUploadedResumes");

// Get previous LinkedIn summaries
export const getAllLinkedInSummaries = () =>
  API.get("/api/linkedin/allLinkedIn");

// Logout helper
export const logout = () => {
  localStorage.removeItem("token");
};
