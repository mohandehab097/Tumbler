package com.social.Tumblr.posts.controllers;

import com.social.Tumblr.posts.models.dtos.StoryDto;
import com.social.Tumblr.posts.models.dtos.StoryViewDto;
import com.social.Tumblr.posts.models.dtos.ViewsDto;
import com.social.Tumblr.posts.models.entities.Story;
import com.social.Tumblr.posts.services.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v0/story")
public class StoryController {


    @Autowired
    private StoryService storyService;

    @PostMapping
    public ResponseEntity<String> createStory(
             Principal currentUser
            ,@RequestPart(value = "imageFile", required = true)  MultipartFile imageFile) {
        String response = storyService.createStory(currentUser, imageFile);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{storyId}")
    public ResponseEntity<StoryViewDto> getUserViewStory(@PathVariable Long storyId,Principal currentUser) {
        StoryViewDto story = storyService.getStoryOfUser(storyId,currentUser);
        return ResponseEntity.ok(story);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<StoryDto>> getFollowedUsersStories(Principal currentUser) {
        List<StoryDto> stories = storyService.getStoriesFromFollowedUsers(currentUser);
        return ResponseEntity.ok(stories);
    }

    @GetMapping("/views/{storyId}")
    public ResponseEntity<List<ViewsDto>> getStoryViewsDetails(@PathVariable Long storyId,Principal currentUser) {
        List<ViewsDto> storyViewsDetails = storyService.getStoryViewsDetails(storyId,currentUser);
        return ResponseEntity.ok(storyViewsDetails);
    }
}
