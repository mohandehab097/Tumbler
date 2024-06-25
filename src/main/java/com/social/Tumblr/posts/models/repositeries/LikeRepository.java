package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Likes;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Likes,Long> {

    int countByPostId(Long postId);
    boolean existsByUserAndPost(Users user, Posts post);
    Optional<Likes> findByUserAndPost(Users user, Posts post);

}
