package com.social.Tumblr.security.services.service;

import com.social.Tumblr.security.models.entities.Users;

public interface TokenService {


    public void saveUserToken(Users user, String jwtToken);

    public void revokeAllUserTokens(Users user);


}
