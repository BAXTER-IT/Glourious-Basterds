package com.crypto.orderbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (userDetails != null && password.equals(userDetails.getPassword())) {
                // Create authentication token
                Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                
                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(auth);
                
                // Create response
                Map<String, Object> response = new HashMap<>();
                Map<String, Object> user = new HashMap<>();
                user.put("username", username);
                
                response.put("success", true);
                response.put("user", user);
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid username or password");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Authentication failed: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear security context
        SecurityContextHolder.clearContext();
        
        // Invalidate session if exists
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Logged out successfully");
        
        return ResponseEntity.ok(responseBody);
    }
}