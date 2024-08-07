package com.social.Tumblr.posts.controllers;


import com.social.Tumblr.posts.models.dtos.PostRequestDto;
import com.social.Tumblr.posts.models.dtos.PostResponseDto;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.services.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestPart("content") @Valid String content
            ,@RequestPart(value = "postImage", required = false) MultipartFile postImage) {
        postService.createPost(currentUser, content,postImage);
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
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId,Principal currentUser) {
        PostResponseDto post = postService.getPostById(postId,currentUser);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<PostResponseDto>> getPostsOfCurrentUserAndFollowers(Principal currentUser) {
        List<PostResponseDto> posts = postService.getPostsOfCurrentUserAndFollowers(currentUser);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/paginated-posts")
    public ResponseEntity<List<PostResponseDto>> getAllPostsWithPagination(Principal currentUser,
                                                                           @RequestParam(defaultValue = "0") int page,
                                                                           @RequestParam(defaultValue = "10") int size) {
        List<PostResponseDto> posts = postService.getAllPostsWithPagination(page,size,currentUser);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/notification-post")
    public ResponseEntity<PostResponseDto> getPostByIdFromNotification(Principal currentUser,
            @RequestParam("postId") Long postId, @RequestParam("notificationId") Long notificationId) {
        PostResponseDto post = postService.getPostByIdFromNotification(postId,notificationId,currentUser);
        return ResponseEntity.ok(post);
    }


}
