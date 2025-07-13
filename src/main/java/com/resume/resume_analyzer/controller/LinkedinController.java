package com.resume.resume_analyzer.controller;

import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.service.interfac.ILinkedinUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/linkedin")
public class LinkedinController {
    @Autowired
    private ILinkedinUploadService iLinkedinUploadService;

    @PostMapping("/upload")
    public ResponseEntity<Response> uploadProfile(
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {

        String profile = body.get("profile");
        Response response = iLinkedinUploadService.uploadLinkedin(profile);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Response> getAll(@AuthenticationPrincipal UserDetails userDetails){
        Response response = iLinkedinUploadService.getAllProfiles();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{profileId}")
    public ResponseEntity<Response> deleteProfile(@PathVariable long profileId,
                                                  @AuthenticationPrincipal UserDetails userDetails){
        Response response = iLinkedinUploadService.deleteProfile(profileId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/analyse/{profileId}")
    public ResponseEntity<Response> analyseProfile(@PathVariable long profileId,
                                                   @AuthenticationPrincipal UserDetails userDetails){
        Response response = iLinkedinUploadService.AnalyseLinkedin(profileId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }



}
