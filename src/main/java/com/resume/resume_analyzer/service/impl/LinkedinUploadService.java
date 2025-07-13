package com.resume.resume_analyzer.service.impl;

import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.LinkedinUpload;
import com.resume.resume_analyzer.entity.User;
import com.resume.resume_analyzer.exception.OurException;
import com.resume.resume_analyzer.repository.LinkedinUploadRepository;
import com.resume.resume_analyzer.repository.UserRepository;
import com.resume.resume_analyzer.service.interfac.ILinkedinUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
@Service
public class LinkedinUploadService implements ILinkedinUploadService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LinkedinUploadRepository linkedinUploadRepository;

    @Autowired
    private OpenAiResumeLinkedinAnalyzerService openAiResumeLinkedinAnalyzerService;

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
    @Override
    public Response uploadLinkedin(String profileUrl) {
        Response response = new Response();
        try {
            if (!profileUrl.startsWith("https://www.linkedin.com/")) {
                throw new OurException("Invalid LinkedIn URL");
            }
            String email = getCurrentUserEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User not found"));

            LinkedinUpload upload = new LinkedinUpload();
            upload.setLinkedinProfile(profileUrl);
            upload.setUploadedAt(LocalDateTime.now());
            upload.setUser(user);

            linkedinUploadRepository.save(upload);

            response.setSuccess(true);
            response.setMessage("LinkedIn profile uploaded successfully");
            response.setData(upload);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to upload LinkedIn profile: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response getAllProfiles() {
        Response response = new Response();
        try {
            String email = getCurrentUserEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User not found"));

            response.setSuccess(true);
            response.setMessage("LinkedIn profiles fetched");
            response.setData(user.getProfiles());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error fetching profiles: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteProfile(long profileId) {
        Response response = new Response();
        try {
            LinkedinUpload profile = linkedinUploadRepository.findById(profileId)
                    .orElseThrow(() -> new OurException("Profile not found"));
            String email = getCurrentUserEmail();
            if (!profile.getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized: Not your profile");
            }
            linkedinUploadRepository.deleteById(profileId);
            response.setSuccess(true);
            response.setMessage("Profile deleted successfully");
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error deleting profile: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response AnalyseLinkedin(long profileId) {
        Response response = new Response();
        try {
            LinkedinUpload profile = linkedinUploadRepository.findById(profileId)
                    .orElseThrow(() -> new OurException("Profile not found"));

            String email = getCurrentUserEmail();
            if (!profile.getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized: Not your profile");
            }

            String profileText = "LinkedIn Profile Summary: " + profile.getLinkedinProfile();
            String prompt = """
            Act as a LinkedIn expert career coach.
            Review the following profile URL and simulate what a recruiter might infer.
            Highlight:
            - Strengths
            - Missing elements
            - Recommendations for improvement
            Profile URL: """ + profileText;

            String analysis = openAiResumeLinkedinAnalyzerService.analyzeText(prompt);

            response.setSuccess(true);
            response.setMessage("LinkedIn profile analyzed");
            response.setData(analysis);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error analyzing profile: " + e.getMessage());
        }
        return response;
    }
}
