package com.social.Tumblr.security.models.dtos.request;


import com.social.Tumblr.security.annotations.ValidPassword;


public class ChangePasswordRequest {

    @ValidPassword
    private String currentPassword;
    @ValidPassword
    private String newPassword;
    @ValidPassword
    private String confirmationPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmationPassword() {
        return confirmationPassword;
    }

    public void setConfirmationPassword(String confirmationPassword) {
        this.confirmationPassword = confirmationPassword;
    }
}

