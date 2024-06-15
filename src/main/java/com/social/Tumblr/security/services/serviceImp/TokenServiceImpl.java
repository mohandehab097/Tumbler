package com.social.Tumblr.security.services.serviceImp;

import com.social.Tumblr.security.models.entities.Token;
import com.social.Tumblr.security.models.entities.Users;
import com.social.Tumblr.security.models.enums.TokenType;
import com.social.Tumblr.security.models.repositeries.TokenRepository;
import com.social.Tumblr.security.services.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    public void saveUserToken(Users user, String jwtToken) {
        Token token = new Token();
        token.setUser(user);
        token.setToken(jwtToken);
        token.setExpired(false);
        token.setTokenType(TokenType.BEARER);
        token.setRevoked(false);
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(Users user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
