package com.social.Tumblr.posts.models.repositeries;

import com.social.Tumblr.posts.models.entities.Notification;
import com.social.Tumblr.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByToUserOrderByCreatedAtDesc(Users toUser);

}
