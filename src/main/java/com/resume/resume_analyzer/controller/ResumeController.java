package com.resume.resume_analyzer.controller;

import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.User;
import com.resume.resume_analyzer.repository.UserRepository;
import com.resume.resume_analyzer.service.interfac.IResumeUploadSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    @Autowired
    private IResumeUploadSevice iResumeUploadSevice;

    @Autowired
    private UserRepository userRepository;
    @PostMapping(value = "/upload")
    public ResponseEntity<Response> resumeUpload(@RequestParam("file") MultipartFile file,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Response response = iResumeUploadSevice.uploadResume(file);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAllResumes(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow();

        // fetch only resumes uploaded by this user
        Response response = iResumeUploadSevice.getResumesByUserId(user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<Response> DeleteResume(@PathVariable  long resumeId,@AuthenticationPrincipal UserDetails userDetails){
        Response response = iResumeUploadSevice.deleteResume(resumeId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/analyze/{resumeId}")
    public ResponseEntity<Response> analyseResume(@PathVariable  long resumeId,@AuthenticationPrincipal UserDetails userDetails){
        Response response = iResumeUploadSevice.analyzeResume(resumeId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

}
