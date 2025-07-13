package com.resume.resume_analyzer.service.impl;

import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.AnalysisResult;
import com.resume.resume_analyzer.entity.ResumeUpload;
import com.resume.resume_analyzer.entity.LinkedinUpload;
import com.resume.resume_analyzer.exception.OurException;
import com.resume.resume_analyzer.repository.AnalysisResultRepository;
import com.resume.resume_analyzer.repository.ResumeUploadRepository;
import com.resume.resume_analyzer.repository.LinkedinUploadRepository;
import com.resume.resume_analyzer.repository.UserRepository;
import com.resume.resume_analyzer.service.interfac.IAnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AnalysisResultService implements IAnalysisResultService {

    @Autowired
    private ResumeUploadRepository resumeRepo;

    @Autowired
    private LinkedinUploadRepository linkedinRepo;

    @Autowired
    private AnalysisResultRepository analysisRepo;

    @Autowired
    private UserRepository userRepository;

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    @Override
    public Response saveAnalysisResult(long resumeId, String aiResponse) {
        Response response = new Response();
        try {
            ResumeUpload resume = resumeRepo.findById(resumeId)
                    .orElseThrow(() -> new OurException("Resume not found"));
            String email = getCurrentUserEmail();
            if (!resume.getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized access to this resume");
            }
            AnalysisResult result = new AnalysisResult();
            result.setResume(resume);
            result.setSummary(aiResponse);
            result.setScore(85);
            result.setSuggestions("Improve formatting. Use action verbs.");

            analysisRepo.save(result);

            response.setSuccess(true);
            response.setMessage("Resume analysis saved.");
            response.setData(result);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error saving resume analysis: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response saveLinkedInAnalysisResult(long profileId, String aiResponse) {
        Response response = new Response();
        try {
            LinkedinUpload profile = linkedinRepo.findById(profileId)
                    .orElseThrow(() -> new OurException("LinkedIn profile not found"));
            String email = getCurrentUserEmail();
            if (!profile.getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized access to this resume");
            }
            AnalysisResult result = new AnalysisResult();
            result.setProfile(profile);
            result.setSummary(aiResponse);
            result.setScore(78); // sample
            result.setSuggestions("Add more quantifiable achievements.");

            analysisRepo.save(result);

            response.setSuccess(true);
            response.setMessage("LinkedIn analysis saved.");
            response.setData(result);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error saving LinkedIn analysis: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getResumeAnalysis(long resumeId) {
        Response response = new Response();
        try {
            AnalysisResult result = analysisRepo.findByResumeId(resumeId);
            if (result == null)
                throw new OurException("No analysis found for this resume");
            String email = getCurrentUserEmail();
            if (!result.getResume().getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized access to this resume analysis");
            }
            response.setSuccess(true);
            response.setData(result);
            response.setMessage("Fetched resume analysis");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to get resume analysis: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getLinkedInAnalysis(long profileId) {
        Response response = new Response();
        try {
            AnalysisResult result = analysisRepo.findByProfileId(profileId);
            if (result == null)
                throw new OurException("No analysis found for this LinkedIn profile");
            String email = getCurrentUserEmail();
            if (!result.getProfile().getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized access to this LinkedIn analysis");
            }
            response.setSuccess(true);
            response.setData(result);
            response.setMessage("Fetched LinkedIn analysis");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to get LinkedIn analysis: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteAnalysis(long analysisId) {
        Response response = new Response();
        try {
            AnalysisResult result = analysisRepo.findById(analysisId)
                    .orElseThrow(() -> new OurException("Analysis  not found"));

            String email = getCurrentUserEmail();
            if (result.getResume() != null && !result.getResume().getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized access to this analysis");
            }
            if (result.getProfile() != null && !result.getProfile().getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized access to this analysis");
            }
            analysisRepo.deleteById(analysisId);
            response.setSuccess(true);
            response.setMessage("Deleted analysis successfully.");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to delete analysis: " + e.getMessage());
        }
        return response;
    }
}
