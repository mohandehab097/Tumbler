package com.social.Tumblr.posts.services.serviceimpl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    private static final Logger log = LoggerFactory.getLogger(FirebaseMessagingService.class);
    @Autowired
    private FirebaseMessaging firebaseMessaging;

    public void sendNotificationToToken(String token, String title, String body) {
        Message message = Message.builder()
                .setNotification(Notification.builder().setBody(body)
                        .setTitle(title).build())
                .setToken(token)
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            log.error("something went wrong while send to firebase");
        }
    }
}