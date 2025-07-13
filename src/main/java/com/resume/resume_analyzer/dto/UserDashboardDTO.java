package com.resume.resume_analyzer.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDashboardDTO {
    private UserDTO user;
    private List<ResumeUploadDTO> resumes;
    private List<LinkedinUploadDTO> linkedInProfiles;
    private List<AnalysisResultDTO> analysisReports;
}
