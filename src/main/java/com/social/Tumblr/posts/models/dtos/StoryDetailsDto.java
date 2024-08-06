package com.social.Tumblr.posts.models.dtos;

public class StoryDetailsDto {

    private Long storyId;
    private String storyImage;
    private Integer userId;
    private String username;
    private String userImage;
    private String timeAgo;
    private boolean isWatched;


    public StoryDetailsDto() {
    }

    public StoryDetailsDto(Long storyId, String storyImage, Integer userId, String username, String userImage, String timeAgo, boolean isWatched) {
        this.storyId = storyId;
        this.storyImage = storyImage;
        this.userId = userId;
        this.username = username;
        this.userImage = userImage;
        this.timeAgo = timeAgo;
        this.isWatched = isWatched;
    }

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public String getStoryImage() {
        return storyImage;
    }

    public void setStoryImage(String storyImage) {
        this.storyImage = storyImage;
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

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }
}
