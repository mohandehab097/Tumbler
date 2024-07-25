package com.social.Tumblr.posts.services.service;


import com.social.Tumblr.posts.models.dtos.CommentRequestDto;
import com.social.Tumblr.posts.models.dtos.CommentResponseDto;
import com.social.Tumblr.posts.models.entities.Comments;
import com.social.Tumblr.security.models.entities.Users;


import java.security.Principal;
import java.util.List;

public interface CommentService {

    public void addComment(Principal currentUser, Long postId, CommentRequestDto commentRequestDto);

    public void editComment(Long commentId, Principal currentUser, CommentRequestDto commentRequestDto);

    public void deleteComment(Long commentId, Principal currentUser);

    public List<CommentResponseDto> getCommentsForPost(Long postId,Principal currentUser);

    public boolean likeComment(Long commentId, Principal currentUser);

    public CommentResponseDto mapCommentToResponseDto(Comments comment, Users currentUser);


}
