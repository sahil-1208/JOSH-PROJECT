package com.josh.service.impl;

import com.josh.dto.request.LoginRequest;
import com.josh.dto.response.ApiResponse;
import com.josh.dto.response.LoginResponse;
import com.josh.entity.session.UserSession;
import com.josh.exception.UserLoginException;
import com.josh.repo.user.UserRepo;
import com.josh.repo.session.UserSessionRepo;
import com.josh.service.AuthenticationService;
import com.josh.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationImpl implements AuthenticationService {

    private final UserRepo userRepository;

    private final UserSessionRepo userSessionRepo;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse logIn(LoginRequest loginRequest) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));

        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() ->
                new UserLoginException("Invalid email or password "));

        var jwt = jwtUtil.generateToken(user);
        var expiresAt = jwtUtil.extractExpiration(jwt)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        storeSession(user.getUsername(),jwt,expiresAt);

        return LoginResponse.builder().token(jwt).build();
    }

    @Override
    public void storeSession(String username, String token, LocalDateTime expiresAt) {
        UserSession userSession = UserSession.builder()
                .username(username).token(token).createdAt(LocalDateTime.now())
                .expiresAt(expiresAt).build();
        userSessionRepo.save(userSession);
    }

    @Override
    public ApiResponse logout(String token) {
        userSessionRepo.findByToken(token)
                .ifPresent(userSessionRepo::delete);
        return  ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("User Logout Successfully !..")
                .build();
    }

    @Override
    public void forceLogoutAllDevices(String username) {
        userSessionRepo.deleteByUsername(username);
    }
}

