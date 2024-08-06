package com.social.Tumblr.posts.services.service;

import com.social.Tumblr.posts.models.entities.Follower;
import com.social.Tumblr.posts.models.enums.FollowStatus;
import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import com.social.Tumblr.security.models.entities.Users;
import jakarta.transaction.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface FollowerService {

    public boolean followUser(Principal currentUser, Integer userId);

    public boolean isFollowing(Principal currentUser, Integer userId);

    public Boolean getFollowStatus(Principal currentUser, Integer userId);

    public List<SearchedUsersResponseDto> getFollowers(Integer userId, Principal currentUser);

    public List<SearchedUsersResponseDto> getFollowing(Integer userId,Principal currentUser);

    public Long getNumberFollowers(Users user);

    public Long getNumberFollowing(Users user);

    public List<Integer> findAllFollowedUserByCurrentUser(Integer currentUserId);

}
