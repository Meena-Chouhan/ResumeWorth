package com.resume.resume_analyzer.service.interfac;

import com.resume.resume_analyzer.dto.LoginRequest;
import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.User;

public interface IUserService {
    Response register(User loginRequest);
    Response login(LoginRequest loginRequest);
    Response getAllUsers();
    Response getUserDashboard(long userId);
    Response deleteUser(String userId);
    Response getUserById(long userId);
    Response getMyInfo(long userId);
}
