package com.social.Tumblr.posts.services.serviceimpl;

import com.social.Tumblr.posts.models.dtos.NotificationDto;
import com.social.Tumblr.posts.models.entities.Notification;
import com.social.Tumblr.posts.models.entities.Posts;
import com.social.Tumblr.posts.models.enums.NotificationType;
import com.social.Tumblr.posts.models.repositeries.NotificationRepository;
import com.social.Tumblr.posts.services.service.GoogleCloudStorageService;
import com.social.Tumblr.posts.services.service.NotificationService;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.repositeries.UserRepository;
import com.social.Tumblr.security.utils.TimeUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    public Notification createNotification(Users fromUser, Users toUser, Posts post, String notifcationMessage, String type) {
        Notification notification = new Notification();
        notification.setFromUser(fromUser);
        notification.setToUser(toUser);
        notification.setPost(post);
        notification.setNotifcationMessage(notifcationMessage);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setType(type);

        return notificationRepository.save(notification);
    }

    public List<NotificationDto> getNotificationsForUser(Principal currentUser) {
        Users user = getUserFromPrincipal(currentUser);
        List<Notification> notifications = notificationRepository.findByToUserOrderByCreatedAtDesc(user);

        return notifications.stream()
                .map(this::mapNotificationToDto)
                .collect(Collectors.toList());
    }

    public void deleteOldNotification(Integer fromUserId, Integer toUserId, String type) {
        notificationRepository.deleteByFromUserIdAndToUserIdAndType(fromUserId, toUserId, type);
    }

    public void deleteOldPostNotification(Integer fromUserId, Integer toUserId, Long postId, String type) {
        notificationRepository.deleteByFromUserIdAndToUserIdAndPostIdAndType(fromUserId, toUserId, postId, type);
    }

    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDto mapNotificationToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        if (notification.getFromUser() != null) {
            dto.setFromUserId(notification.getFromUser().getId());
            dto.setFromUserName(notification.getFromUser().getFullName());
            if (notification.getFromUser().getImage() != null) {
                String userImage = googleCloudStorageService.getFileUrl(notification.getFromUser().getImage());
                dto.setFromUserImage(userImage);
            }
        }
        if (notification.getToUser() != null) {
            dto.setToUserId(notification.getToUser().getId());
            dto.setToUserName(notification.getToUser().getFullName());
        }
        if (notification.getPost() != null) {
            dto.setPostId(notification.getPost().getId());
        }
        dto.setNotificationMessage(notification.getNotifcationMessage());
        dto.setTimeAgo(TimeUtil.calculateTimeAgo(notification.getCreatedAt()));
        dto.setRead(notification.isRead());

        if (notification.getPost() != null) {
            if (notification.getPost().getImageUrl() != null) {
                String image = googleCloudStorageService.getFileUrl(notification.getPost().getImageUrl());
                dto.setPostImage(image);
            }
        }

        return dto;
    }

    private Users getUserFromPrincipal(Principal currentUser) {
        return (Users) ((UsernamePasswordAuthenticationToken) currentUser).getPrincipal();
    }
}
