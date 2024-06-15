package com.social.Tumblr.security.models.dtos.response;

public class SearchedUsersResponseDto {

    private String fullName;
    private String image;


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
