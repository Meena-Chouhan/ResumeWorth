package com.resume.resume_analyzer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "resume_uploads")
public class ResumeUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @NotBlank(message = "Resume file name or path is required.")
    private String resumeFilePath;

    @NotBlank(message = "S3 Key is required")
    @Column(name = "s3_key", nullable = false)
    private String s3Key;

}
