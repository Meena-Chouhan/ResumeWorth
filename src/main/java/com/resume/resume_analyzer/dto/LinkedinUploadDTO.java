package com.resume.resume_analyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.security.PrivateKey;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LinkedinUploadDTO {
    private Long id;
    private LocalDateTime uploadedAt;
    private String LinkedinProfile;

}
