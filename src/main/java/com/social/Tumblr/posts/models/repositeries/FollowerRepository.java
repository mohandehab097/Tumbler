package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Follower;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    Optional<Follower> findByFollowerAndFollowing(Users follower, Users following);

    @Query("SELECT f.id FROM Follower f WHERE f.follower.id = :followerId and f.following.id = :followingId")
    Long existFollowing(Integer followerId, Integer followingId);


    boolean existsByFollowerAndFollowing(Users follower, Users following);

    List<Follower> findByFollower(Users follower);

    List<Follower> findByFollowing(Users following);

    @Query("SELECT f.following.id FROM Follower f WHERE f.follower.id = :userId")
    List<Integer> findAllFollowedUserByCurrentUser(@Param("userId") Integer userId);
}
