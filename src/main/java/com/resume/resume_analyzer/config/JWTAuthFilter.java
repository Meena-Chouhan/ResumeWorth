package com.resume.resume_analyzer.config;

import com.resume.resume_analyzer.utils.JWTutils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTutils jwTutils;
    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }


        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final  String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        if (jwtToken.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            userEmail = jwTutils.extractUsername(jwtToken);
        } catch (Exception e) {
            System.out.println(" Invalid token format: " + e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }


        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if(jwTutils.validateToken(jwtToken,userDetails)){
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        filterChain.doFilter(request,response);
    }
}
