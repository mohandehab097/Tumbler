package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Comments;
import com.social.Tumblr.posts.models.entities.LikeComment;
import com.social.Tumblr.posts.models.entities.Likes;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    Optional<LikeComment> findByUserAndComment(Users user, Comments comment);

    boolean existsByUserAndComment(Users user, Comments comment);

    long countByComment(Comments comment);

    List<LikeComment> findByComment(Comments comments);

}

