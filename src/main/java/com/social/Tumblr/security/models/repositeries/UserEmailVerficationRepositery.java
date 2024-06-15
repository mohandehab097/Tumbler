package com.social.Tumblr.security.models.repositeries;

import com.social.Tumblr.security.models.entities.UserEmailVerfication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEmailVerficationRepositery  extends JpaRepository<UserEmailVerfication,Long> {

    public Optional<UserEmailVerfication> findByVerificationOtp(String otp);

}
