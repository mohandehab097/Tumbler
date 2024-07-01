package com.social.Tumblr.posts.services.service;


import com.social.Tumblr.posts.models.dtos.CommentRequestDto;
import com.social.Tumblr.posts.models.dtos.CommentResponseDto;


import java.security.Principal;
import java.util.List;

public interface CommentService {

    public void addComment(Principal currentUser, Long postId, CommentRequestDto commentRequestDto);

    public void editComment(Long commentId, Principal currentUser, CommentRequestDto commentRequestDto);

    public void deleteComment(Long commentId, Principal currentUser);

    public List<CommentResponseDto> getCommentsForPost(Long postId);

    public void likeComment(Long commentId, Principal currentUser);

    public void unlikeComment(Long commentId, Principal currentUser);


}
