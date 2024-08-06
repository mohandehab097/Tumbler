package com.social.Tumblr.posts.controllers;

import com.social.Tumblr.posts.services.service.FollowerService;
import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v0/followers")
public class FollowerController {
    @Autowired
    private FollowerService followerService;


    @PostMapping("/follow/{userId}")
    public ResponseEntity<String> followUser(Principal currentUser, @PathVariable Integer userId) {
        if (followerService.followUser(currentUser, userId)) {
            return ResponseEntity.ok("Followed successfully");
        } else {
            return ResponseEntity.ok("Unfollowed successfully");
        }
    }


    @GetMapping("/followers-details/{userId}")
    public ResponseEntity<List<SearchedUsersResponseDto>> getFollowers(@PathVariable Integer userId , Principal currentUser) {
        List<SearchedUsersResponseDto> followers = followerService.getFollowers(userId,currentUser);
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following-details/{userId}")
    public ResponseEntity<List<SearchedUsersResponseDto>> getFollowing(@PathVariable Integer userId , Principal currentUser) {
        List<SearchedUsersResponseDto> following = followerService.getFollowing(userId,currentUser);
        return ResponseEntity.ok(following);
    }


}
