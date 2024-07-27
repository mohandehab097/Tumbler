package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Long> {

    Story findByIdAndExpiresAtAfter(Long storyId, LocalDateTime now);

    @Query("SELECT s FROM Story s WHERE s.user.id IN :userIds AND s.expiresAt > :currentTime")
    List<Story> findAllByUserIdsAndExpiresAtAfter(@Param("userIds") List<Integer> userIds, @Param("currentTime") LocalDateTime currentTime);

    Story findByUserIdAndExpiresAtAfter(Integer userId, LocalDateTime now);


}
