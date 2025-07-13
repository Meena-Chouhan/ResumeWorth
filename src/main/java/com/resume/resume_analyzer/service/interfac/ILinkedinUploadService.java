package com.resume.resume_analyzer.service.interfac;

import com.resume.resume_analyzer.dto.Response;

public interface ILinkedinUploadService {
    Response uploadLinkedin(String profileUrl);
    Response getAllProfiles();
    Response deleteProfile(long profileId);
    Response AnalyseLinkedin(long profileId);
}
