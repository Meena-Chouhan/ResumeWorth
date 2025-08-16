import React, { useEffect, useState } from "react";
import {
  getAllResumeSummaries,
  getAllLinkedInSummaries,
  analyzeResume,
  analyzeLinkedIn,
  deleteResume,
  deleteLinkedIn,logout 
} from "../services/api";
import { useNavigate } from "react-router-dom";
import "./Dashboard.css";

export default function Dashboard() {
  const [resumes, setResumes] = useState([]);
  const [linkedIns, setLinkedIns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [analyzingId, setAnalyzingId] = useState(null);
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }

    async function fetchData() {
      try {
        const res1 = await getAllResumeSummaries();
        const res2 = await getAllLinkedInSummaries();
        setResumes(res1.data.data || []);
        setLinkedIns(res2.data.data || []);
      } catch (err) {
        console.error("Error loading dashboard data:", err);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [navigate, token]);

  const handleAnalyzeResume = async (id) => {
  setAnalyzingId(id);
  try {
    const res = await analyzeResume(id);
    const updated = resumes.map((resume) =>
      resume.id === id ? { ...resume, analysis: res.data.data } : resume
    );
    setResumes(updated);
  } catch (err) {
    console.error("Resume analysis failed:", err);
    alert("Failed to analyze resume.");
  } finally {
    setAnalyzingId(null);
  }
};


 const handleAnalyzeLinkedIn = async (id) => {
  setAnalyzingId(id);
  try {
    const res = await analyzeLinkedIn(id);
    const updated = linkedIns.map((profile) =>
      profile.id === id ? { ...profile, analysis: res.data.data } : profile
    );
    setLinkedIns(updated);
  } catch (err) {
    console.error("LinkedIn analysis failed:", err);
    alert("Failed to analyze LinkedIn profile.");
  } finally {
    setAnalyzingId(null);
  }
};

  const handleDeleteResume = async (id) => {
    const confirm = window.confirm("Are you sure you want to delete this resume?");
  if (!confirm) return;
    try {
      await deleteResume(id);
      setResumes(resumes.filter((resume) => resume.id !== id));
    } catch (err) {
      console.error("Failed to delete resume:", err);
      alert("Could not delete resume.");
    }
  };

  const handleDeleteLinkedIn = async (id) => {
    const confirm = window.confirm("Are you sure you want to delete this LinkedIn profile?");
  if (!confirm) return;
    try {
      await deleteLinkedIn(id);
      setLinkedIns(linkedIns.filter((profile) => profile.id !== id));
    } catch (err) {
      console.error("Failed to delete LinkedIn profile:", err);
      alert("Could not delete LinkedIn profile.");
    }
  };


const handleLogout = () => {
  logout();
  navigate('/login'); 
};

  if (loading) return <p>Loading dashboard...</p>;

  return (
    <div className="dashboard-container">
      <h2>Welcome to your Dashboard</h2>

      {role === "ROLE_ADMIN" && (
        <div className="admin-section">
          <h3>Admin Tools</h3>
          <p>(Only visible to admins)</p>
        </div>
      )}

      <hr />
      <h3>Your Uploaded Resumes</h3>
      {resumes.length === 0 ? (
        <p>No resumes uploaded yet.</p>
      ) : (
        <ul className="list-group">
          {resumes.map((resume) => (
            <li key={resume.id} className="list-item">
              <a
                href={resume.resumeFilePath}
                target="_blank"
                rel="noreferrer"
              >
                View Resume
              </a>{" "}
               Uploaded: {new Date(resume.uploadedAt).toLocaleString()}
              <br />
              <button className="ana"
                onClick={() => handleAnalyzeResume(resume.id)}
                disabled={analyzingId === resume.id}
              >
                {analyzingId === resume.id ? "Analyzing..." : "Analyze"}
              </button>
              <button className="delete"
                onClick={() => handleDeleteResume(resume.id)}
                style={{ marginLeft: "10px" }}
              >
                Delete
              </button>
              {resume.analysis && (
                <div className="analysis-block">
                  <strong>Resume Analysis:</strong>
                  <pre>{resume.analysis}</pre>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}

      <hr />
      <h3>Your LinkedIn Profiles</h3>
      {linkedIns.length === 0 ? (
        <p>No LinkedIn profiles submitted yet.</p>
      ) : (
        <ul className="list-group">
          {linkedIns.map((profile) => (
            <li key={profile.id} className="list-item">
              <a
                href={profile.linkedinProfile}
                target="_blank"
                rel="noreferrer"
              >
                View LinkedIn Profile
              </a>{" "}
               Uploaded: {new Date(profile.uploadedAt).toLocaleString()}
              <br />
              <button className="ana"
                onClick={() => handleAnalyzeLinkedIn(profile.id)}
                disabled={analyzingId === profile.id}
              >
                {analyzingId === profile.id
                  ? "Analyzing..."
                  : "Analyze"}
              </button>
              <button className="delete"
                onClick={() => handleDeleteLinkedIn(profile.id)}
                style={{ marginLeft: "10px" }}
              >
                Delete
              </button>
              

              {profile.analysis && (
                <div className="analysis-block">
                  <strong>LinkedIn Analysis:</strong>
                  <pre>{profile.analysis}</pre>
                </div>
              )}
            </li>
          ))}
        </ul>
      )}
      <div className="out"><button onClick={handleLogout}>Logout</button></div>
    </div>
  );
}
