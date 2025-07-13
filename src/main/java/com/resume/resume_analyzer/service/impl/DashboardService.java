package com.resume.resume_analyzer.service.impl;

import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.dto.UserDashboardDTO;
import com.resume.resume_analyzer.entity.User;
import com.resume.resume_analyzer.exception.OurException;
import com.resume.resume_analyzer.repository.UserRepository;
import com.resume.resume_analyzer.service.interfac.IDashboardService;
import com.resume.resume_analyzer.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class DashboardService implements IDashboardService {
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
    public Response getUserDashboard() {
        Response response = new Response();
        try {
            String email = getCurrentUserEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("User not found"));

            UserDashboardDTO dashboardDTO = new UserDashboardDTO();
            dashboardDTO.setUser(Utils.UserToUserDto(user));
            dashboardDTO.setResumes(user.getResumes().stream()
                    .map(Utils::toResumeUploadDTO).toList());
            dashboardDTO.setLinkedInProfiles(user.getProfiles().stream()
                    .map(Utils::toLinkedinUploadDTO).toList());

            response.setData(dashboardDTO);
            response.setMessage("Dashboard data fetched");
            response.setSuccess(true);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

}
