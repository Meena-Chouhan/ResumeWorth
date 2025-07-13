package com.resume.resume_analyzer.service.impl;
import com.resume.resume_analyzer.dto.Response;

import com.resume.resume_analyzer.dto.LoginRequest;
import com.resume.resume_analyzer.dto.UserDashboardDTO;
import com.resume.resume_analyzer.entity.User;
import com.resume.resume_analyzer.exception.OurException;
import com.resume.resume_analyzer.repository.UserRepository;
import com.resume.resume_analyzer.service.interfac.IUserService;
import com.resume.resume_analyzer.utils.JWTutils;
import com.resume.resume_analyzer.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTutils jwTutils;
    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public Response register(User register) {
        Response response = new Response();
        try {
            if (userRepository.existsByEmail(register.getEmail())) {
                throw new OurException("User already exists with this email!");
            }


            // Encode password
            register.setPassword(passwordEncoder.encode(register.getPassword()));

            // Save to DB
            User savedUser = userRepository.save(register);

            // Generate JWT
            String token = jwTutils.generateToken(savedUser);

            response.setMessage("User registered successfully!");
            response.setToken(token);
            response.setData(savedUser); // optional
            response.setSuccess(true);
            return response;
        } catch (OurException e) {
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        } catch (Exception e) {
            response.setMessage("Something went wrong during registration.");
            response.setSuccess(false);
        }

        return response;
    }


    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try{
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new OurException("Invalid email or password"));

            if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
                throw new OurException("Invalid Email or Password");
            }
            String token = jwTutils.generateToken(user);
            Map<String, Object> data = new HashMap<>();
            data.put("user", user); 
            data.put("token", token);

            response.setSuccess(true);
            response.setData(token);
            response.setMessage("Login Successful");
            response.setToken(token);
            return response;
        }
        catch (OurException e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        catch (Exception e){
            response.setSuccess(false);
            response.setMessage("Login Failed");
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try{
            List<User> users = userRepository.findAll();
            response.setData(users);
            response.setMessage("Users fetched successfully.");
            response.setSuccess(true);
            return response;
        }catch (Exception  e){
            response.setMessage("Failed to fetch users.");
            response.setSuccess(false);
        }
        return response;
    }


    @Override
    public Response getUserDashboard(long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found"));

            UserDashboardDTO dashboardDTO = new UserDashboardDTO();
            dashboardDTO.setUser(Utils.UserToUserDto(user));
            assert user != null;
            dashboardDTO.setResumes(user.getResumes().stream()
                    .map(Utils::toResumeUploadDTO)
                    .collect(Collectors.toList()));
            dashboardDTO.setLinkedInProfiles(user.getProfiles().stream()
                    .map(Utils::toLinkedinUploadDTO)
                    .collect(Collectors.toList()));

            response.setSuccess(true);
            response.setMessage("Dashboard loaded successfully.");
            response.setData(dashboardDTO);
        } catch (OurException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Something went wrong while loading dashboard.");
        }

        return response;
    }


    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();
        try{
            User user = userRepository.findByEmail(userId)
                    .orElseThrow(() -> new OurException("User not found"));
            userRepository.delete(user);
            response.setSuccess(true);
            response.setData(userRepository.findAll());
            response.setMessage("User Deleted Successfully.");
            return response;
        }
        catch (OurException e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("An error occurred while deleting the user.");
        }
        return response;
    }

    @Override
    public Response getUserById(long userId) {
        Response response = new Response();
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found with ID: " + userId));;
            response.setData(user);
            response.setMessage("User Found");
            response.setSuccess(true);
        }catch (OurException e){
            response.setMessage(e.getMessage());
            response.setSuccess(false);
        }catch (Exception e){
            response.setMessage("Something went wrong while fetching the user.");
            response.setSuccess(false);
        }
        return response;
    }

    @Override
    public Response getMyInfo(long userId) {
        Response response = new Response();
        try{
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User not found with ID: " + userId));
            response.setData(user);
            response.setMessage("User fetched successfully.");
            response.setSuccess(true);
        }
        catch (OurException e){
            response.setMessage("User fetched successfully.");
            response.setSuccess(true);
        }
        catch (Exception e){
            response.setMessage("Something went wrong while fetching user info.");
            response.setSuccess(false);
        }
        return response;
    }
}
