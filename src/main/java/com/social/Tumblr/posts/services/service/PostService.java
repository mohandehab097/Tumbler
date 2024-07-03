package com.social.Tumblr.posts.services.service;

import com.social.Tumblr.posts.models.dtos.PostRequestDto;
import com.social.Tumblr.posts.models.dtos.PostResponseDto;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface PostService {


    public void createPost(Principal currentUser, String content, MultipartFile postImage);

    public void updatePost(Long postId, Principal currentUser, PostRequestDto postRequestDto);

    public void deletePost(Long postId, Principal currentUser);

    public List<PostResponseDto> getAllPostsForCurrentUser(Principal currentUser);

    public List<PostResponseDto> getAllPostsForUser(Integer userId,Principal currentUser);

    public PostResponseDto getPostById(Long postId,Principal currentUser);

    public Long getNumberOfPosts(Users users);

    public List<PostResponseDto> getPostsOfCurrentUserAndFollowers(Principal currentUser);

    public List<PostResponseDto> getAllPostsWithPagination(int page, int size,Principal currentUser);


    }
