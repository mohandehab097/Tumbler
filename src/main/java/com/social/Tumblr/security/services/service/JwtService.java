package com.social.Tumblr.security.services.service;


import org.springframework.security.core.userdetails.UserDetails;
import java.util.Map;

public interface JwtService {

    public String extractUserName(String token);

    public boolean isTokenValid(String token, UserDetails userDetails);

    public String generateToken(UserDetails userDetails);

    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails);




}
