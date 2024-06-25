package com.social.Tumblr.posts.controllers;

import com.social.Tumblr.posts.services.service.FollowerService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.services.service.UserService;
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
            return ResponseEntity.ok("Already following");
        }
    }

    @PostMapping("/unfollow/{userId}")
    public ResponseEntity<String> unfollowUser(Principal currentUser, @PathVariable Integer userId) {

        if (followerService.unfollowUser(currentUser, userId)) {
            return ResponseEntity.ok("Unfollowed successfully");
        } else {
            return ResponseEntity.ok("Not following");
        }
    }

//    @GetMapping("/followers")
//    public ResponseEntity<List<Users>> getFollowers(Principal currentUser) {
//        List<Users> followers = followerService.getFollowers(currentUser);
//        return ResponseEntity.ok(followers);
//    }
//
//    @GetMapping("/following")
//    public ResponseEntity<List<Users>> getFollowing(Principal currentUser) {
//        List<Users> following = followerService.getFollowing(user);
//        return ResponseEntity.ok(following);
//    }


}
