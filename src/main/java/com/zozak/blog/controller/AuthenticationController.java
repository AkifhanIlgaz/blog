package com.zozak.blog.controller;

import com.zozak.blog.domain.dto.AuthResponse;
import com.zozak.blog.domain.dto.LoginRequest;
import com.zozak.blog.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @RequestBody LoginRequest loginRequest
    ) {
        UserDetails user = authenticationService.authenticate(
            loginRequest.getEmail(),
            loginRequest.getPassword()
        );

        AuthResponse authResponse = AuthResponse.builder()
            .token(authenticationService.generateToken(user))
            .expiresIn(86400) // 24 hours in seconds
            .build();

        return ResponseEntity.ok(authResponse);
    }
}
