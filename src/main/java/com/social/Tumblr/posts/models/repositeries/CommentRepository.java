package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Long> {

    int countByPostId(Long postId);
    List<Comments> findByPostId(Long postId);

}
