package com.resume.resume_analyzer.service.interfac;
import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.ResumeUpload;
import com.resume.resume_analyzer.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface IResumeUploadSevice {
    Response uploadResume(MultipartFile file);

    Response getAllResumes();
    Response analyzeResume(long resumeId);
    Response deleteResume(long resumeId);
    Response getResumesByUserId(User user);
}
