package com.social.Tumblr.security.services.service;

import com.social.Tumblr.security.models.dtos.request.LoginRequestDto;
import com.social.Tumblr.security.models.dtos.response.AuthenticationResponseDto;
import com.social.Tumblr.security.models.dtos.request.RegisterRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {

    public void register(RegisterRequestDto registerRequestDto, MultipartFile imageFile);

    public AuthenticationResponseDto login(LoginRequestDto loginRequestDto);

    public String verifyEmail(String token);

}
