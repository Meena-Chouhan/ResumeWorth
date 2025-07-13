package com.resume.resume_analyzer.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AnalysisResultDTO {
    private Long id;
    private String summary;
    private int score;
    private String suggestions;

    private ResumeUploadDTO resume;
    private LinkedinUploadDTO profile;
}
