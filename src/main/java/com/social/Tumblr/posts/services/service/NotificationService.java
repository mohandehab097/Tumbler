package com.social.Tumblr.posts.services.service;

import com.social.Tumblr.posts.models.dtos.NotificationDto;
import com.social.Tumblr.posts.models.entities.Notification;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.models.enums.NotificationType;
import com.social.Tumblr.security.models.entities.Users;

import java.security.Principal;
import java.util.List;

public interface NotificationService {

    public List<NotificationDto> getNotificationsForUser(Principal currentUser);

    public void markNotificationAsRead(Long notificationId);

    public Notification createNotification(Users fromUser, Users toUser, Posts post,String notificationMessage);


    }
