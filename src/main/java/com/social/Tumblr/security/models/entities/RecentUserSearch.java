package com.social.Tumblr.security.models.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recent_user_search")
public class RecentUserSearch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "searched_user_id", nullable = false)
    private Users searchedUser;

    @Column(name = "search_time", nullable = false)
    private LocalDateTime searchTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Users getSearchedUser() {
        return searchedUser;
    }

    public void setSearchedUser(Users searchedUser) {
        this.searchedUser = searchedUser;
    }

    public LocalDateTime getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(LocalDateTime searchTime) {
        this.searchTime = searchTime;
    }


    // getters and setters
}
