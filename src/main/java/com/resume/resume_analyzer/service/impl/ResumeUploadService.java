package com.resume.resume_analyzer.service.impl;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.ResumeUpload;
import com.resume.resume_analyzer.entity.User;
import com.resume.resume_analyzer.exception.OurException;
import com.resume.resume_analyzer.repository.ResumeUploadRepository;
import com.resume.resume_analyzer.repository.UserRepository;
import com.resume.resume_analyzer.service.interfac.IResumeUploadSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResumeUploadService implements IResumeUploadSevice {

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private ResumeUploadRepository resumeUploadRepository;

    @Autowired
    private OpenAiResumeLinkedinAnalyzerService openAiResumeLinkedinAnalyzerService;
    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Override
    public Response uploadResume(MultipartFile file) {
        Response response = new Response();
        try {
            // 1. Get current logged-in user
            String email = getCurrentUserEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("Invalid email or password"));

            // 2. Generate a unique filename
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // 3. Prepare file metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            // 4. Upload the file to S3
            amazonS3.putObject(bucketName, filename, file.getInputStream(), metadata);
            String fileUrl = amazonS3.getUrl(bucketName, filename).toString();

            // 5. Save resume details in the database
            ResumeUpload resumeUpload = new ResumeUpload();
            resumeUpload.setResumeFilePath(fileUrl);
            resumeUpload.setS3Key(filename);
            resumeUpload.setUploadedAt(LocalDateTime.now());
            resumeUpload.setUser(user);
            resumeUploadRepository.save(resumeUpload);

            // 6. Build successful response
            response.setMessage("Resume uploaded to S3 successfully!");
            response.setSuccess(true);
            response.setData(fileUrl);

        } catch (IOException e) {
            response.setMessage("Failed to upload to S3: " + e.getMessage());
            response.setSuccess(false);
        } catch (OurException e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        } catch (Exception e) {
            response.setMessage("Unexpected error during upload.");
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public Response getAllResumes() {
        Response response = new Response();
        try{
            String email = getCurrentUserEmail();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new OurException("Invalid email or password"));
            response.setData( user.getResumes());
            response.setMessage("Resumes fetched successfully");
            response.setSuccess(true);
        }
        catch (OurException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Something went wrong while fetching resumes");
        }

        return response;
    }

    private String extractTextFromPdf(InputStream inputStream) {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (IOException e) {
            throw new OurException("Failed to extract text from resume PDF.");
        }
    }

    @Override
    public Response analyzeResume(long resumeId) {
        Response response = new Response();
        try {
            ResumeUpload resume = resumeUploadRepository.findById(resumeId)
                    .orElseThrow(() -> new OurException("Resume not found"));

            String email = getCurrentUserEmail();
            if (!resume.getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized access to this resume");
            }

            String s3Key = resume.getS3Key();
            S3Object s3Object = amazonS3.getObject(bucketName, s3Key);
            InputStream resumeStream = s3Object.getObjectContent();
            String extractedText = extractTextFromPdf(resumeStream);

            String prompt = """
You are a career coach and resume expert. 
Please analyze the following resume and provide:

1. Strengths (with explanation).
2. Weaknesses (with reasoning and suggestions).
3. Improvements (with concrete, actionable steps).
Make the explanation detailed and written in an encouraging, human-friendly tone.

Here is the resume content:
""" + extractedText;

            String aiResponse = openAiResumeLinkedinAnalyzerService.analyzeText(prompt);
            resumeUploadRepository.save(resume);

            response.setData(aiResponse);
            response.setMessage("Resume analyzed and saved successfully!");
            response.setSuccess(true);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error analyzing resume: " + e.getMessage());
        }
        return response;
    }


    @Override
    public Response deleteResume(long resumeId) {
        Response response = new Response();
        try{
            ResumeUpload resume = resumeUploadRepository.findById(resumeId)
                    .orElseThrow(() -> new OurException("Resume not found with ID: " + resumeId));
            String email = getCurrentUserEmail();
            if (!resume.getUser().getEmail().equals(email)) {
                throw new OurException("Unauthorized to delete this resume");
            }
            String s3Key = resume.getS3Key();
            amazonS3.deleteObject(bucketName, s3Key);
            resumeUploadRepository.deleteById(resumeId);
            response.setMessage("Resumes Deleted successfully");
            response.setSuccess(true);
        }
        catch (OurException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Something went wrong while Deleting resumes");
        }

        return response;
    }

    @Override
    public Response getResumesByUserId(User user) {
        List<ResumeUpload> resumes = resumeUploadRepository.findByUser(user);
        return new Response("Fetched user resumes", true, resumes, null);
    }

}
