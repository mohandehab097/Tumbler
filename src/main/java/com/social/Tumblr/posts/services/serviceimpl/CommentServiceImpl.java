package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.exceptions.ResourceNotFoundException;
import com.social.Tumblr.posts.exceptions.UnauthorizedException;
import com.social.Tumblr.posts.models.dtos.CommentRequestDto;
import com.social.Tumblr.posts.models.dtos.CommentResponseDto;
import com.social.Tumblr.posts.models.entities.Comments;
import com.social.Tumblr.posts.models.entities.LikeComment;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.models.repositeries.CommentRepository;
import com.social.Tumblr.posts.models.repositeries.LikeCommentRepository;
import com.social.Tumblr.posts.models.repositeries.PostRepository;
import com.social.Tumblr.posts.services.service.CommentService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {


    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeCommentRepository likeCommentRepository;

    public void addComment(Principal currentUser, Long postId, CommentRequestDto commentRequestDto) {
        Users user = getUserFromPrincipal(currentUser);
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Comments comment = new Comments();
        comment.setContent(commentRequestDto.getContent());
        comment.setUser(user);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    public void editComment(Long commentId, Principal currentUser, CommentRequestDto commentRequestDto) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        Users user = getUserFromPrincipal(currentUser);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to edit this comment.");
        }

        comment.setContent(commentRequestDto.getContent());
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, Principal currentUser) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

        Users user = getUserFromPrincipal(currentUser);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponseDto> getCommentsForPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::mapCommentToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void likeComment(Long commentId, Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Optional<LikeComment> existingLike = likeCommentRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            throw new IllegalStateException("You have already liked this comment");
        }

        LikeComment like = new LikeComment();
        like.setUser(user);
        like.setComment(comment);
        like.setCreatedDate(LocalDateTime.now());
        likeCommentRepository.save(like);
    }

    @Override
    public void unlikeComment(Long commentId, Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        LikeComment like = likeCommentRepository.findByUserAndComment(user, comment)
                .orElseThrow(() -> new EntityNotFoundException("Like not found"));

        likeCommentRepository.delete(like);
    }


    private CommentResponseDto mapCommentToResponseDto(Comments comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        return dto;
    }

    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }
}


