package com.social.Tumblr.security.models.dtos.response;

import com.social.Tumblr.posts.models.enums.FollowStatus;

public class UserProfileResponseDto {

    private Integer id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String image;
    private String bio;
    private boolean isFollow;
    private Long NumberOfFollowers;
    private Long NumberOfFollowing;
    private Long NumberOfPosts;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Long getNumberOfFollowers() {
        return NumberOfFollowers;
    }

    public void setNumberOfFollowers(Long numberOfFollowers) {
        NumberOfFollowers = numberOfFollowers;
    }

    public Long getNumberOfFollowing() {
        return NumberOfFollowing;
    }

    public void setNumberOfFollowing(Long numberOfFollowing) {
        NumberOfFollowing = numberOfFollowing;
    }

    public Long getNumberOfPosts() {
        return NumberOfPosts;
    }

    public void setNumberOfPosts(Long numberOfPosts) {
        NumberOfPosts = numberOfPosts;
    }
}
