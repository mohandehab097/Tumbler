package com.social.Tumblr.posts.models.dtos;

import java.util.List;

public class StoryDto {

    private boolean isStoryUploaded;
    private List<StoryDetailsDto> storyDetails;

    public List<StoryDetailsDto> getStoryDetails() {
        return storyDetails;
    }

    public void setStoryDetails(List<StoryDetailsDto> storyDetails) {
        this.storyDetails = storyDetails;
    }

    public boolean isStoryUploaded() {
        return isStoryUploaded;
    }

    public void setStoryUploaded(boolean storyUploaded) {
        isStoryUploaded = storyUploaded;
    }
}
