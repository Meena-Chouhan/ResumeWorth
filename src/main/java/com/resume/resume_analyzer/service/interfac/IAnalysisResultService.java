package com.resume.resume_analyzer.service.interfac;

import com.resume.resume_analyzer.dto.Response;

public interface IAnalysisResultService {
    Response saveAnalysisResult(long resumeId, String aiResponse);
    Response getResumeAnalysis(long resumeId);
    Response getLinkedInAnalysis(long profileId);
    Response saveLinkedInAnalysisResult(long profileId,String aiResponse);
    Response deleteAnalysis(long analysisId);


}
