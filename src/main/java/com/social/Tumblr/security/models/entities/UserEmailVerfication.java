package com.social.Tumblr.security.models.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserEmailVerfication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "verification_otp",nullable = false)
    private String verificationOtp;
    @Column(name = "email_verified")
    private boolean emailVerified;
    @Column(name = "otp_generated_time")
    private LocalDateTime otpGeneratedTime;
    @Column(name = "otp_expiration_time")
    private LocalDateTime otpExpirationTime;

    @ManyToOne
    @JoinColumn(nullable = false,name = "verfied_user_id")
    private Users user;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVerificationOtp() {
        return verificationOtp;
    }

    public void setVerificationOtp(String verificationOtp) {
        this.verificationOtp = verificationOtp;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public LocalDateTime getOtpGeneratedTime() {
        return otpGeneratedTime;
    }

    public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
        this.otpGeneratedTime = otpGeneratedTime;
    }

    public LocalDateTime getOtpExpirationTime() {
        return otpExpirationTime;
    }

    public void setOtpExpirationTime(LocalDateTime otpExpirationTime) {
        this.otpExpirationTime = otpExpirationTime;
    }
}
