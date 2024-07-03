package com.social.Tumblr.security.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtil {

    public static String calculateTimeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else if (days < 7) {
            return days + " days ago";
        } else {
            return createdAt.format(Constants.DATE_FORMATTER);
        }
    }

}
