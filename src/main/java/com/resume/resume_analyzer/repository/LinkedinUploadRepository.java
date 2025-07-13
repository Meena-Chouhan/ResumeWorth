package com.resume.resume_analyzer.repository;

import com.resume.resume_analyzer.entity.LinkedinUpload;
import com.resume.resume_analyzer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinkedinUploadRepository extends JpaRepository<LinkedinUpload,Long> {
    LinkedinUpload findByLinkedinProfile(String linkedinProfile);
    List<LinkedinUpload> findByUser(User user);;
}
