package com.resume.resume_analyzer.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name = "Analysis")
public class AnalysisResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String summary;
    private int score;

    @Column(length = 2000)
    private String suggestions;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    private ResumeUpload resume;

    @ManyToOne
    @JoinColumn(name = "Linkedin_id")
    private LinkedinUpload profile;




}
