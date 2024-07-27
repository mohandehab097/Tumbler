package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.models.entities.Follower;
import com.social.Tumblr.posts.models.enums.FollowStatus;
import com.social.Tumblr.posts.models.repositeries.FollowerRepository;
import com.social.Tumblr.posts.services.service.FollowerService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.services.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowerServiceImpl implements FollowerService {

    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private UserService userService;


    @Transactional
    public boolean followUser(Principal currentUser, Integer userId) {

        Users follower = getUserFromPrincipal(currentUser);
        Users following = userService.getUserById(userId);

        if (follower.getId().equals(following.getId())) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }

        Optional<Follower> existingFollower = followerRepository.findByFollowerAndFollowing(follower, following);
        if (existingFollower.isPresent()) {
            followerRepository.delete(existingFollower.get());
            return false;
        }

        try {
            Follower newFollower = new Follower();
            newFollower.setFollower(follower);
            newFollower.setFollowing(following);
            followerRepository.save(newFollower);
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

    public boolean getFollowStatus(Principal currentUser, Integer userId) {
        Users currentUserEntity = getUserFromPrincipal(currentUser);
        Users profileUser = userService.getUserById(userId);

        boolean isFollowing = followerRepository.existsByFollowerAndFollowing(currentUserEntity, profileUser);

        return isFollowing;
}

public List<Users> getFollowers(Users user) {
    List<Follower> followers = followerRepository.findByFollowing(user);
    return followers.stream()
            .map(Follower::getFollower)
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
