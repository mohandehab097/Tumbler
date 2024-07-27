package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Story;
import com.social.Tumblr.posts.models.entities.StoryView;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryViewRepository extends JpaRepository<StoryView, Long> {

    Optional<StoryView> findByStoryAndUser(Story story, Users user);

    @Query("SELECT COUNT(sv) FROM StoryView sv WHERE sv.story = :story")
    Integer countByStory(@Param("story") Story story);

    @Query("SELECT sv FROM StoryView sv WHERE sv.story.id = :storyId")
    List<StoryView> findAllByStoryId(@Param("storyId") Long storyId);

}