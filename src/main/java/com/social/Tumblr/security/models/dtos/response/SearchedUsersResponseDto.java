package com.social.Tumblr.security.models.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.social.Tumblr.posts.models.enums.FollowStatus;

public class SearchedUsersResponseDto {

    private Integer id;
    private String fullName;
    private String image;
    private FollowStatus followStatus;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public FollowStatus getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(FollowStatus followStatus) {
        this.followStatus = followStatus;
    }
}
