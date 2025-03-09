package com.josh.controller;

import com.josh.dto.request.LoginRequest;
import com.josh.dto.response.ApiResponse;
import com.josh.dto.response.LoginResponse;
import com.josh.exception.UserLogoutException;
import com.josh.service.AuthenticationService;
import com.josh.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/user")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authenticationService.logIn(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(HttpServletRequest request) {
        String token = jwtUtil.extractToken(request);
        if (token == null || token.isEmpty()) {
            throw new UserLogoutException("Invalid logout request: No token provided");
        }
        return ResponseEntity.ok().body(authenticationService.logout(token));
    }
}
