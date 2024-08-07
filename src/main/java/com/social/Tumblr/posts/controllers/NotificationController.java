package com.social.Tumblr.posts.controllers;

import com.social.Tumblr.posts.models.dtos.NotificationDto;
import com.social.Tumblr.posts.services.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v0/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(Principal currentUser) {
        List<NotificationDto> notifications = notificationService.getNotificationsForUser(currentUser);
        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<?> deleteStory(@PathVariable Long notificationId, Principal currentUser) {
        notificationService.deleteById(notificationId);
        return ResponseEntity.ok("Story deleted successfully");
    }

}
