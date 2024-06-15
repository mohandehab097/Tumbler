package com.social.Tumblr.security.services.serviceImp;


import com.social.Tumblr.security.mail.EmailService;
import com.social.Tumblr.security.models.entities.UserEmailVerfication;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.repositeries.UserEmailVerficationRepositery;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import com.social.Tumblr.security.services.service.EmailVerificationService;
import com.social.Tumblr.security.utils.HtmlGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@Transactional
public class EmailVerificationServiceImpl implements EmailVerificationService {

    @Autowired
    private UserEmailVerficationRepositery userEmailVerificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    @Value("${verify.email.endpoint.url}")
    private String verifyEmailEndpointUrl;

    @Override
    public String SaveVerificationOtp(Users user) {
        String randomOtp = generateRandomOtp();
        UserEmailVerfication userEmailVerification = new UserEmailVerfication();
        userEmailVerification.setVerificationOtp(randomOtp);
        userEmailVerification.setOtpGeneratedTime(LocalDateTime.now());
        userEmailVerification.setOtpExpirationTime(LocalDateTime.now().plusMinutes(15));
        userEmailVerification.setUser(user);
        userEmailVerificationRepository.save(userEmailVerification);
        return randomOtp;
    }

    private String generateRandomOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a random number between 100000 and 999999
        return String.valueOf(otp);
    }

    @Override
    public void emailVerification(Users user, String otp) {
        String verificationUrl = verifyEmailEndpointUrl + otp;
        emailService.send(user.getEmail(), HtmlGenerator.buildEmailHtmlUi(user.getFullName(), verificationUrl));
    }

    @Override
    public String verifyEmail(String otp) {
        UserEmailVerfication userEmailVerification = getUserEmailVerification(otp);
        Users user = userEmailVerification.getUser();

        String htmlResponse = validateVerification(user, userEmailVerification);

        user.setEmailVerified(true);
        userRepository.save(user);

        return htmlResponse.isEmpty() ? HtmlGenerator.getHtmlSuccessPage() :
                htmlResponse;
    }

    private UserEmailVerfication getUserEmailVerification(String otp) {
        return userEmailVerificationRepository.findByVerificationOtp(otp)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid token"));
    }

    private String validateVerification(Users user, UserEmailVerfication userEmailVerification) {
        String errorHtmlResponse = "";
        if (user.isEnabled()) {
            errorHtmlResponse = HtmlGenerator.getHtmlErrorPage("This account is already verified. Please login.");
        } else if (userEmailVerification.getOtpExpirationTime().isBefore(LocalDateTime.now())) {
            errorHtmlResponse = HtmlGenerator.getHtmlErrorPage("Otp is expired. Please generate another OTP.");
        }

        return errorHtmlResponse;
    }
}
