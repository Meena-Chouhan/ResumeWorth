package com.resume.resume_analyzer.controller;

import com.resume.resume_analyzer.dto.LoginRequest;
import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.User;
import com.resume.resume_analyzer.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private IUserService iUserService;
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user ){
        Response response = iUserService.register(user);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        Response response = iUserService.login(loginRequest);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
