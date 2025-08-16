import React, { useState } from 'react';
import axios from 'axios';
import API from '../api';
import './Upload.css';

export default function Upload() {
  const [resume, setResume] = useState(null);
  const [linkedinUrl, setLinkedinUrl] = useState('');
  const [message, setMessage] = useState('');
  const [uploading, setUploading] = useState(false);

  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');

  const handleResumeUpload = (e) => {
    setResume(e.target.files[0]);
  };

  const handleSubmitResume = async () => {
    if (!resume) return setMessage('Please select a resume');

    setUploading(true);
    const formData = new FormData();
    formData.append('file', resume);

    try {
      const response = await axios.post(
        `http://localhost:8082/api/resume/upload/${userId}`,
        formData,
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'multipart/form-data',
          },
        }
      );
      setMessage('Resume uploaded and analyzed successfully!');
    } catch (error) {
      console.error(error);
      setMessage('Failed to upload resume');
    }
    setUploading(false);
  };

  const handleSubmitLinkedin = async () => {
    if (!linkedinUrl) return setMessage('Please enter a LinkedIn URL');

    setUploading(true);
    try {
      const response = await axios.post(
        `http://localhost:8082/api/linkedin/analyze/${userId}`,
        { url: linkedinUrl },
        {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );
      setMessage('LinkedIn URL submitted and analyzed successfully!');
    } catch (error) {
      console.error(error);
      setMessage('Failed to analyze LinkedIn URL');
    }
    setUploading(false);
  };

  return (
    <div className="upload-container">
      <h2>Upload Resume / LinkedIn</h2>

      <div className="upload-section">
        <label>Upload Resume (PDF):</label>
        <input type="file" accept=".pdf" onChange={handleResumeUpload} />
        <button onClick={handleSubmitResume} disabled={uploading}>
          Upload Resume
        </button>
      </div>

      <div className="upload-section">
        <label>Enter LinkedIn URL:</label>
        <input
          type="text"
          value={linkedinUrl}
          onChange={(e) => setLinkedinUrl(e.target.value)}
          placeholder="https://linkedin.com/in/username"
        />
        <button onClick={handleSubmitLinkedin} disabled={uploading}>
          Submit LinkedIn URL
        </button>
      </div>

      {message && <div className="upload-message">{message}</div>}
    </div>
  );
}
