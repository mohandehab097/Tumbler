package com.social.Tumblr.posts.services.service;

import com.social.Tumblr.posts.exceptions.ResourceNotFoundException;
import com.social.Tumblr.posts.models.entities.Likes;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;

public interface LikeService {

    public void likePost(Principal currentUser, Long postId);

    public void unlikePost(Principal currentUser, Long postId);

}
