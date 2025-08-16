// src/services/utils/tokenUtils.js

// Save token to local storage
export const setToken = (token) => {
  localStorage.setItem('token', token);
};

// Get token from local storage
export const getToken = () => {
  return localStorage.getItem('token');
};

// Remove token from local storage
export const removeToken = () => {
  localStorage.removeItem('token');
};
