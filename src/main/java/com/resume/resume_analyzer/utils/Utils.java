package com.resume.resume_analyzer.utils;

import com.resume.resume_analyzer.dto.AnalysisResultDTO;
import com.resume.resume_analyzer.dto.LinkedinUploadDTO;
import com.resume.resume_analyzer.dto.ResumeUploadDTO;
import com.resume.resume_analyzer.dto.UserDTO;
import com.resume.resume_analyzer.entity.AnalysisResult;
import com.resume.resume_analyzer.entity.LinkedinUpload;
import com.resume.resume_analyzer.entity.ResumeUpload;
import com.resume.resume_analyzer.entity.User;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final SecureRandom secureRandom = new SecureRandom();

    public static String getRandomAlphanumeric(int length){
        StringBuilder stringBuilder = new StringBuilder();
        int randomindex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
        int randomchar = ALPHANUMERIC_STRING.charAt(randomindex);
        stringBuilder.append(randomchar);
        return stringBuilder.toString();
    }

    public static UserDTO UserToUserDto(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());

        if(!user.getResumes().isEmpty()){
            userDTO.setResumes(user.getResumes()
                    .stream()
                    .map(Utils :: toResumeUploadDTO)
                    .collect(Collectors.toList()));
        }
        if(!user.getProfiles().isEmpty()){
            userDTO.setProfiles(user.getProfiles()
                    .stream()
                    .map(Utils::toLinkedinUploadDTO)
                    .collect(Collectors.toList()));
        }
        return userDTO;
    }

    public static ResumeUploadDTO toResumeUploadDTO(ResumeUpload resume){
        ResumeUploadDTO resumeUploadDTO = new ResumeUploadDTO();
        resumeUploadDTO.setId(resume.getId());
        resumeUploadDTO.setUploadedAt(resume.getUploadedAt());
        resumeUploadDTO.setResumeFilePath(resume.getResumeFilePath());
        return resumeUploadDTO;
    }

    public static LinkedinUploadDTO toLinkedinUploadDTO(LinkedinUpload profiles){
        LinkedinUploadDTO linkedinUploadDTO = new LinkedinUploadDTO();
        linkedinUploadDTO.setId(profiles.getId());
        linkedinUploadDTO.setLinkedinProfile(profiles.getLinkedinProfile());
        linkedinUploadDTO.setUploadedAt(profiles.getUploadedAt());
        return linkedinUploadDTO;
    }
    public static AnalysisResultDTO toanalysisResultDTO(AnalysisResult analysisResult){
        AnalysisResultDTO analysisResultDTO = new AnalysisResultDTO();
        analysisResultDTO.setId(analysisResult.getId());
        analysisResultDTO.setSummary(analysisResult.getSummary());
        analysisResultDTO.setSuggestions(analysisResult.getSuggestions());
        analysisResultDTO.setScore(analysisResult.getScore());
        analysisResultDTO.setProfile(toLinkedinUploadDTO(analysisResult.getProfile()));
        analysisResultDTO.setResume(toResumeUploadDTO(analysisResult.getResume()));
        return analysisResultDTO;
    }

}
