package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Notification;
import com.social.Tumblr.security.models.entities.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByToUserOrderByCreatedAtDesc(Users toUser);

    @Transactional
    @Modifying
    @Query(value = "delete from notification where from_user_id =:fromUserId and to_user_id =:toUserId and type=:type", nativeQuery = true)
    void deleteByFromUserIdAndToUserIdAndType(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId, @Param("type") String type);


    @Transactional
    @Modifying
    @Query(value = "delete from notification where from_user_id =:fromUserId and to_user_id =:toUserId and post_id = :postId and type=:type", nativeQuery = true)
    void deleteByFromUserIdAndToUserIdAndPostIdAndType(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId
            , @Param("postId") Long postId
            , @Param("type") String type);

}
