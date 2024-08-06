package com.social.Tumblr.posts.models.enums;

public enum NotificationType {
    LIKE("like"),
    COMMENT("comment"),
    LIKE_COMMENT("likeComment"),
    FOLLOWING("following");

    private String type;

    NotificationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
