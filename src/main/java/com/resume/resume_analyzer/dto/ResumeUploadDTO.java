package com.resume.resume_analyzer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResumeUploadDTO {
    private Long id;
    private LocalDateTime uploadedAt;
    private String resumeFilePath;



}
