package com.social.Tumblr.posts.controllers;


import com.social.Tumblr.posts.models.dtos.PostRequestDto;
import com.social.Tumblr.posts.models.dtos.PostResponseDto;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.services.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v0/posts")
public class PostsController {


    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Posts> createPost(
            Principal currentUser,
            @RequestBody PostRequestDto postRequestDto) {
        postService.createPost(currentUser, postRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Posts> updatePost(
            @PathVariable Long postId,
            Principal currentUser,
            @RequestBody PostRequestDto postRequestDto) {
        postService.updatePost(postId, currentUser, postRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Principal currentUser) {
        postService.deletePost(postId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/current_user-posts")
    public ResponseEntity<List<PostResponseDto>> getAllPostsForCurrentUser(Principal currentUser) {
        List<PostResponseDto> posts = postService.getAllPostsForCurrentUser(currentUser);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getAllPostsForUser(@PathVariable Integer userId,Principal currentUser) {
        List<PostResponseDto> posts = postService.getAllPostsForUser(userId,currentUser);
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<PostResponseDto>> getPostsOfCurrentUserAndFollowers(Principal currentUser) {
        List<PostResponseDto> posts = postService.getPostsOfCurrentUserAndFollowers(currentUser);
        return ResponseEntity.ok(posts);
    }


}
