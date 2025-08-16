import React, { useState } from 'react';
import axios from 'axios';
import './LinkedInSubmit.css';

const LinkedInSubmit = () => {
  const [url, setUrl] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async () => {
    if (!url.trim()) {
      alert('Please enter a valid LinkedIn URL.');
      return;
    }

    const token = localStorage.getItem('token');
    setLoading(true);

    try {
      const response = await axios.post(
        'http://localhost:8082/api/linkedin/upload',
        { profile: url },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        }
      );

      setMessage(response.data.message || 'LinkedIn profile uploaded!');
    } catch (error) {
      console.error('Upload failed:', error);
      setMessage('Upload failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="linkedin-submit-container">
      <h2>Upload LinkedIn Profile</h2>
      <input
        type="text"
        value={url}
        onChange={(e) => setUrl(e.target.value)}
        placeholder="Enter LinkedIn Profile URL"
      />
      <button onClick={handleSubmit} disabled={loading}>
        {loading ? 'Uploading...' : 'Upload'}
      </button>
      {message && <p>{message}</p>}
    </div>
  );
};

export default LinkedInSubmit;
