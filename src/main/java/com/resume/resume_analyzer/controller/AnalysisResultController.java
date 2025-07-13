package com.resume.resume_analyzer.controller;

import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.service.interfac.IAnalysisResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisResultController {
    @Autowired
    private IAnalysisResultService ianalysisResultService;

    @PostMapping("/save/{resumeid}")
    public ResponseEntity<Response> saveAnalysis(@PathVariable("resumeid") long resumeId,
                                                 @RequestParam("aiResponse") String aiResponse, @AuthenticationPrincipal UserDetails userDetails){
        Response response = ianalysisResultService.saveAnalysisResult(resumeId,aiResponse);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getresumeanalysis/{resumeid}")
    public ResponseEntity<Response> resumeAnalysis(@PathVariable("resumeid") long resumeId,@AuthenticationPrincipal UserDetails userDetails){
        Response response = ianalysisResultService.getResumeAnalysis(resumeId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getlinkedinanalysis/{profileid}")
    public ResponseEntity<Response> linkedinAnalysis(@PathVariable("profileid") long profileId, @AuthenticationPrincipal UserDetails userDetails){
        Response response = ianalysisResultService.getLinkedInAnalysis(profileId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteanalysis/{analysisid}")
    public ResponseEntity<Response> deleteAnalysis(@PathVariable("analysisid") long analysisId , @AuthenticationPrincipal UserDetails userDetails){
        Response response = ianalysisResultService.deleteAnalysis(analysisId);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }



}