import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8082",
  headers: {
    "Content-Type": "application/json",
  },
});

// Automatically add token from localStorage
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// ===== AUTH =====
export const loginUser = (credentials) => API.post("/auth/login", credentials);
export const registerUser = (data) => API.post("/auth/register", data);

// ===== DASHBOARD =====
export const fetchDashboard = (userId) => API.get(`/user/DashBoard/${userId}`);
export const fetchUserInfo = (userId) => API.get(`/user/myInfo/${userId}`);

// ===== RESUME =====
export const uploadResume = (formData) =>
  API.post("/api/resume/upload", formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

export const getAllResumeSummaries = () => API.get("/api/resume/all");
export const analyzeResume = (resumeId) => API.get(`/api/resume/analyze/${resumeId}`);
export const deleteResume = (resumeId) => API.delete(`/api/resume/delete/${resumeId}`);




// ===== LINKEDIN =====
export const uploadLinkedIn = (data) => API.post("/api/linkedin/upload", data);
export const getAllLinkedInSummaries = () => API.get("/api/linkedin/all");
export const analyzeLinkedIn = (profileId) => API.get(`/api/linkedin/analyse/${profileId}`);
export const deleteLinkedIn = (profileId) => API.delete(`/api/linkedin/delete/${profileId}`);

// ===== LOGOUT =====
export const logout = () => {
  localStorage.removeItem("token");
  localStorage.removeItem("userId");
  localStorage.removeItem("role");
};
