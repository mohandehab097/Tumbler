package com.social.Tumblr.security.models.repositeries;

import com.social.Tumblr.security.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByPhoneNumber(String phoneNumber);

    Optional<Users> findByFullName(String phoneNumber);

    @Query("SELECT user FROM Users user WHERE UPPER(user.fullName) LIKE UPPER(CONCAT('%', :username, '%')) AND user.id <> :id")
    List<Users> findUsersByUserName(@Param("username") String username, @Param("id") Integer id);


}
