package com.resume.resume_analyzer.controller;

import com.resume.resume_analyzer.dto.Response;
import com.resume.resume_analyzer.entity.User;
import com.resume.resume_analyzer.repository.UserRepository;
import com.resume.resume_analyzer.service.impl.UserService;
import com.resume.resume_analyzer.service.interfac.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iuserService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/DashBoard")
    public ResponseEntity<Response> getDashBoard(@PathVariable long userId,@AuthenticationPrincipal
    UserDetails userDetails)
    {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(iuserService.getUserDashboard(user.getId()));
    }
    @GetMapping("/myInfo")
    public ResponseEntity<Response> myInfo(@PathVariable long userId,@AuthenticationPrincipal UserDetails userDetails){
        Response response = iuserService.getMyInfo(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
