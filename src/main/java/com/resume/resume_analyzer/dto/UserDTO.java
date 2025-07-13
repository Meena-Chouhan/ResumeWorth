package com.resume.resume_analyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {

    private Long id;
    private String email;
    private String username;
    private String role;

    private List<ResumeUploadDTO> resumes = new ArrayList<>();
    private List<LinkedinUploadDTO> profiles = new ArrayList<>();


}
