package com.social.Tumblr.security.services.service;

import com.social.Tumblr.security.models.entities.Users;

public interface EmailVerificationService {

    public String SaveVerificationOtp(Users user);

    public void emailVerification(Users user, String otp);

    public String verifyEmail(String otp);


}
