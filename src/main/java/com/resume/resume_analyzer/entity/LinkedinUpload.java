package com.resume.resume_analyzer.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Linkedin_Profiles")
public class LinkedinUpload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Column(name = "linkedin_profile")
    private String linkedinProfile;


}
