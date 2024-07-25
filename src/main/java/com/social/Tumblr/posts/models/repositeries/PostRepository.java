package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long>, PagingAndSortingRepository<Posts, Long> {


    @Query("SELECT p FROM Posts p WHERE p.user.id = :userId OR p.user.id IN " +
            "(SELECT f.following.id FROM Follower f WHERE f.follower.id = :userId)")
    List<Posts> findPostsByUserIdOrFollowing(@Param("userId") Integer userId);

    List<Posts> findByUserId(Integer userId);

    Page<Posts> findAllByOrderByCreatedAtDateDesc(Pageable pageable);


}
