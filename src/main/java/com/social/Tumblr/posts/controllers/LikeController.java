package com.social.Tumblr.posts.controllers;

import com.social.Tumblr.posts.services.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/v0/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, Principal currentUser) {
        likeService.likePost(currentUser, postId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
