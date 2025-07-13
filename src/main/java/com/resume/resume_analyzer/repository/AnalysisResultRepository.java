package com.resume.resume_analyzer.repository;

import com.resume.resume_analyzer.entity.AnalysisResult;
import com.resume.resume_analyzer.entity.ResumeUpload;
import com.resume.resume_analyzer.entity.LinkedinUpload;
import com.resume.resume_analyzer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, Long> {
    AnalysisResult findByResumeId(long resumeId);
    AnalysisResult findByProfileId(long profileId);
}
