package com.social.Tumblr.posts.models.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDto {

    private Long id;
    private String content;
    private Integer userId;
    private String username;
    private String userImage;
    private String timeAgo;
    private boolean isLiked;
    private long numberOfLikes;
    private List<LikeResponseDto> likeResponseDtos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public long getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(long numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public List<LikeResponseDto> getLikeResponseDtos() {
        return likeResponseDtos;
    }

    public void setLikeResponseDtos(List<LikeResponseDto> likeResponseDtos) {
        this.likeResponseDtos = likeResponseDtos;
    }
}
