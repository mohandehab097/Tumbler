package com.social.Tumblr.security.models.repositeries;

import com.social.Tumblr.security.models.entities.RecentUserSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RecentUserSearchRepository extends JpaRepository<RecentUserSearch,Long> {


    void deleteByUserIdAndSearchedUserId(Integer userId,Integer searchedUserId);

    List<RecentUserSearch> findByUserIdOrderBySearchTimeDesc(Integer userId);

    @Query("SELECT r FROM RecentUserSearch r WHERE r.user.id = :userId AND r.searchedUser.id = :searchedUserId")
    Optional<RecentUserSearch> findByUserIdAndSearchedUserId(@Param("userId") Integer userId, @Param("searchedUserId") Integer searchedUserId);

}
