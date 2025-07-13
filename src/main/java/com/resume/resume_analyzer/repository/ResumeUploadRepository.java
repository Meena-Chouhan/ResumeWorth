package com.resume.resume_analyzer.repository;

import com.resume.resume_analyzer.entity.ResumeUpload;
import com.resume.resume_analyzer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeUploadRepository extends JpaRepository<ResumeUpload, Long> {
    ResumeUpload findResumeByResumeFilePath(String resumeFilePath);
    List<ResumeUpload> findByUser(User user);
    void deleteById(ResumeUpload resumeid);
}
