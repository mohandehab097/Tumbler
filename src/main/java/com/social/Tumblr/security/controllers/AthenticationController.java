package com.social.Tumblr.security.controllers;

import com.social.Tumblr.security.models.dtos.request.LoginRequestDto;
import com.social.Tumblr.security.models.dtos.request.RegisterRequestDto;
import com.social.Tumblr.security.services.service.AuthenticationService;
import jakarta.validation.Valid;
import com.social.Tumblr.security.models.dtos.response.AuthenticationResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v0/auth")
public class AthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestPart("registerRequestDto") @Valid RegisterRequestDto registerRequestDto
            , @RequestPart("imageFile") MultipartFile imageFile) {
        authenticationService.register(registerRequestDto, imageFile);
        return ResponseEntity.ok("Registration successful. Please verify your email.");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.login(loginRequestDto));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        String response = authenticationService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }


}
