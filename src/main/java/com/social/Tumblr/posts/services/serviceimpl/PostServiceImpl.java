package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.exceptions.ResourceNotFoundException;
import com.social.Tumblr.posts.exceptions.UnauthorizedException;
import com.social.Tumblr.posts.models.dtos.CommentResponseDto;
import com.social.Tumblr.posts.models.dtos.PostRequestDto;
import com.social.Tumblr.posts.models.dtos.PostResponseDto;
import com.social.Tumblr.posts.models.entities.Comments;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.models.mappers.PostMapper;
import com.social.Tumblr.posts.models.repositeries.CommentRepository;
import com.social.Tumblr.posts.models.repositeries.LikeRepository;
import com.social.Tumblr.posts.models.repositeries.PostRepository;
import com.social.Tumblr.posts.services.service.PostService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import com.social.Tumblr.security.services.service.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ImagesService imagesService;
    @Value("${image.path}")
    private String imagePath;

    public void createPost(Principal currentUser, String content, MultipartFile postImage) {
        Users user = getUserFromPrincipal(currentUser);
        String imageFileName = imagesService.uploadImage(imagePath,postImage);
            Posts posts = new Posts();
            posts.setContent(content);
            posts.setImageUrl(imageFileName);
            posts.setUser(user);
            postRepository.save(posts);
    }

    public void updatePost(Long postId, Principal currentUser, PostRequestDto postRequestDto) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        Users postUser = post.getUser();
        Users user = getUserFromPrincipal(currentUser);
        if (!postUser.getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to update this post.");
        }

        if (postRequestDto != null) {
            post.setContent(postRequestDto.getContent());
            postRepository.save(post);
        }
    }

    public void deletePost(Long postId, Principal currentUser) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        Users user = getUserFromPrincipal(currentUser);
        if (!post.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to delete this post.");
        }

        postRepository.delete(post);
    }

    public List<PostResponseDto> getAllPostsForCurrentUser(Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        List<Posts> posts = postRepository.findByUserId(user.getId());
        return posts.stream().map(post-> mapToPostResponseDto(post,user)).collect(Collectors.toList());
    }

    public List<PostResponseDto> getAllPostsForUser(Integer userId,Principal currentUser) {
        List<Posts> posts = postRepository.findByUserId(userId);
        Users user = getUserFromPrincipal(currentUser);
        return posts.stream().map(post-> mapToPostResponseDto(post,user)).collect(Collectors.toList());
    }

    public PostResponseDto getPostById(Long postId) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));
        return mapToPostResponseDto(post,null);
    }

    public Long getNumberOfPosts(Users users){
        List<Posts> posts = postRepository.findByUserId(users.getId());
        return (long) posts.size();
    }

    public List<PostResponseDto> getPostsOfCurrentUserAndFollowers(Principal currentUser){
        Users user = getUserFromPrincipal(currentUser);
        List<Posts> posts = postRepository.findPostsByUserIdOrFollowing(user.getId());
        return posts.stream().map(post-> mapToPostResponseDto(post,user)).collect(Collectors.toList());
    }

    public List<PostResponseDto> getAllPostsWithPagination(int page, int size,Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        Pageable pageable = PageRequest.of(page, size);
        Page<Posts> postPage = postRepository.findAll(pageable);
        return postPage.stream()
                .map(post -> mapToPostResponseDto(post,user)) // Convert to DTO
                .collect(Collectors.toList());
    }


    private PostResponseDto mapToPostResponseDto(Posts post,Users currentUser) {
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setContent(post.getContent());
        postResponseDto.setImage(post.getImageUrl());
        postResponseDto.setImage("/" + imagePath + post.getImageUrl());
        postResponseDto.setUsername(post.getUser().getUsername());
        postResponseDto.setNumberOfLikes(likeRepository.countByPostId(post.getId()));
        postResponseDto.setNumberOfComments(commentRepository.countByPostId(post.getId()));
        postResponseDto.setComments(post.getComments().stream().map(this::mapToCommentResponseDto).collect(Collectors.toList()));
        postResponseDto.setLiked(isPostLikedByUser(post, currentUser));
        return postResponseDto;
    }

    private boolean isPostLikedByUser(Posts post, Users user) {
        return likeRepository.existsByUserAndPost(user, post);
    }

    private CommentResponseDto mapToCommentResponseDto(Comments comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setContent(comment.getContent());
        commentResponseDto.setUserId(comment.getUser().getId());
        commentResponseDto.setUsername(comment.getUser().getUsername());
        commentResponseDto.setCreatedAtDate(comment.getCreatedAtDate());
        return commentResponseDto;
    }

    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }

}
