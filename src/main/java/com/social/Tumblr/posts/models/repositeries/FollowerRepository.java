package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Follower;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    Optional<Follower> findByFollowerAndFollowing(Users follower, Users following);

    boolean existsByFollowerAndFollowing(Users follower, Users following);

    List<Follower> findByFollower(Users follower);

    List<Follower> findByFollowing(Users following);
}
