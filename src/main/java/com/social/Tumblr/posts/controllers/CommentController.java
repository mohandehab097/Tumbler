package com.social.Tumblr.posts.controllers;

import com.social.Tumblr.posts.models.dtos.CommentRequestDto;
import com.social.Tumblr.posts.models.dtos.CommentResponseDto;
import com.social.Tumblr.posts.services.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v0/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;


    @PostMapping("/post/{postId}")
    public ResponseEntity<Void> addComment(@PathVariable Long postId, Principal currentUser,
                                           @RequestBody CommentRequestDto commentRequestDto) {
        commentService.addComment(currentUser, postId, commentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Void> editComment(@PathVariable Long commentId, Principal currentUser,
                                            @RequestBody CommentRequestDto commentRequestDto) {
        commentService.editComment(commentId, currentUser, commentRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Principal currentUser) {
        commentService.deleteComment(commentId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForPost(@PathVariable Long postId, Principal currentUser) {
        List<CommentResponseDto> comments = commentService.getCommentsForPost(postId, currentUser);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId, Principal currentUser) {
        if(commentService.likeComment(commentId, currentUser)){
            return new ResponseEntity<>("Liked Comment successfully", HttpStatus.CREATED);
        } else{
            return new ResponseEntity<>("UnLiked Comment successfully", HttpStatus.CREATED);
        }
    }


}
