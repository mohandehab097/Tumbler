package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.models.entities.Follower;
import com.social.Tumblr.posts.models.enums.NotificationType;
import com.social.Tumblr.posts.models.repositeries.FollowerRepository;
import com.social.Tumblr.posts.services.service.FollowerService;
import com.social.Tumblr.posts.services.service.NotificationService;
import com.social.Tumblr.security.models.dtos.response.SearchedUsersResponseDto;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.services.service.UserService;
import com.social.Tumblr.security.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;


    @Override
    public boolean followUser(Principal currentUser, Integer userId) {

        Users follower = getUserFromPrincipal(currentUser);
        Users following = userService.getUserById(userId);

        if (follower.getId().equals(following.getId())) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        Long existingFollowing = followerRepository.existFollowing(follower.getId(), following.getId());

        if (existingFollowing != null) {
            followerRepository.deleteById(existingFollowing);
            return false;
        }

        try {
            Follower newFollower = new Follower();
            newFollower.setFollower(follower);
            newFollower.setFollowing(following);
            followerRepository.save(newFollower);
            String followerFirstName = Utility.findFirstNameOfUser(follower.getFullName());
            notificationService.deleteOldNotification(follower.getId(), following.getId(), NotificationType.FOLLOWING.getType());
            notificationService.createNotification(follower, following, null, followerFirstName + " started following you.", NotificationType.FOLLOWING.getType());

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("User has already followed this user", e);
        }
        return true;
    }


    public boolean isFollowing(Principal currentUser, Integer userId) {
        Users follower = getUserFromPrincipal(currentUser);
        Users following = userService.getUserById(userId);
        return followerRepository.findByFollowerAndFollowing(follower, following).isPresent();
    }

    public Boolean getFollowStatus(Principal currentUser, Integer userId) {
        Users currentUserEntity = getUserFromPrincipal(currentUser);
        Users profileUser = userService.getUserById(userId);

        Boolean isFollowing = followerRepository.existsByFollowerAndFollowing(currentUserEntity, profileUser);

        return isFollowing;
    }


    @Override
    public List<SearchedUsersResponseDto> getFollowers(Integer userId, Principal currentUser) {
        Users profileUser = userService.getUserById(userId);
        Users currentUserEntity = getUserFromPrincipal(currentUser);

        List<Follower> followers = followerRepository.findByFollowing(profileUser);

        if (followers.isEmpty()) {
            return Collections.emptyList();
        }

        return followers.stream()
                .map(Follower::getFollower)
                .map(follower -> {
                    SearchedUsersResponseDto dto = userService.mapUserSearchToSearchedUserDto(follower, currentUser);
                    if (follower.getId().equals(currentUserEntity.getId())) {
                        dto.setFollow(null);
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<SearchedUsersResponseDto> getFollowing(Integer userId, Principal currentUser) {
        Users profileUser = userService.getUserById(userId);
        Users currentUserEntity = getUserFromPrincipal(currentUser);

        List<Follower> followings = followerRepository.findByFollower(profileUser);

        if (followings.isEmpty()) {
            return Collections.emptyList();
        }

        return followings.stream()
                .map(Follower::getFollowing)
                .map(following -> {
                    SearchedUsersResponseDto dto = userService.mapUserSearchToSearchedUserDto(following, currentUser);
                    if (following.getId().equals(currentUserEntity.getId())) {
                        dto.setFollow(null);
                    }
                    return dto;
                })
                .collect(Collectors.toList());

    }


    public Long getNumberFollowers(Users user) {
        List<Follower> followers = followerRepository.findByFollowing(user);
        return followers.stream()
                .map(Follower::getFollower).count();
    }

    public List<Users> getFollowing(Users user) {
        List<Follower> following = followerRepository.findByFollower(user);
        return following.stream()
                .map(Follower::getFollowing)
                .collect(Collectors.toList());
    }

    public Long getNumberFollowing(Users user) {
        List<Follower> following = followerRepository.findByFollower(user);
        return following.stream()
                .map(Follower::getFollowing).count();
    }

    public List<Integer> findAllFollowedUserByCurrentUser(Integer currentUserId) {
        return followerRepository.findAllFollowedUserByCurrentUser(currentUserId);
    }

    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }

}
