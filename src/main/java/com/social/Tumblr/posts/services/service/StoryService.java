package com.social.Tumblr.posts.services.service;

import com.social.Tumblr.posts.models.dtos.StoryDto;
import com.social.Tumblr.posts.models.dtos.StoryViewDto;
import com.social.Tumblr.posts.models.dtos.ViewsDto;
import com.social.Tumblr.posts.models.entities.Story;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface StoryService {


    public String createStory(Principal currentUser, MultipartFile image);

    public List<StoryDto> getStoriesFromFollowedUsers(Principal currentUser);

    public StoryViewDto getStoryOfUser(Long storyId,Principal currentUser);

    public List<ViewsDto> getStoryViewsDetails(Long storyId , Principal currentUser);

    public void deleteById(Long storyId);

}
