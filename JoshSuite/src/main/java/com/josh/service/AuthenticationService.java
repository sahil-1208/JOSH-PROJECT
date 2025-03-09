package com.josh.service;

import com.josh.dto.request.LoginRequest;
import com.josh.dto.response.ApiResponse;
import com.josh.dto.response.LoginResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface AuthenticationService {
    LoginResponse logIn(LoginRequest loginRequest);

    void storeSession(String username, String token, LocalDateTime expiresAt);

    ApiResponse logout(String token);

    void forceLogoutAllDevices(String username);
}