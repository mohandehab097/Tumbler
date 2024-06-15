package com.social.Tumblr.security.models.dtos.request;

import com.social.Tumblr.security.annotations.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserProfileUpdateRequestDto {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Size(max = 15, message = "Phone number should not be longer than 15 characters")
    @ValidPhoneNumber
    private String phoneNumber;
    private String bio;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
