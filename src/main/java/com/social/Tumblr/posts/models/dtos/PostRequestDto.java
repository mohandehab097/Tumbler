package com.social.Tumblr.posts.models.dtos;

import jakarta.persistence.Column;

import java.io.Serializable;

public class PostRequestDto implements Serializable {

    private String content;

    private String imageUrl;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
