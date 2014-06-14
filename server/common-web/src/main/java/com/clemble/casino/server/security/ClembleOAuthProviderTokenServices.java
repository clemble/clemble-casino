package com.clemble.casino.server.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;

import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;
import org.springframework.security.oauth.provider.token.RandomValueProviderTokenServices;

public class ClembleOAuthProviderTokenServices extends RandomValueProviderTokenServices {

    // TODO track unused tokens

    final private TokenRepository tokenRepository;

    public ClembleOAuthProviderTokenServices(TokenRepository tokenRepository) {
        this.tokenRepository = checkNotNull(tokenRepository);
        this.setAccessTokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(7));
    }

    @Override
    protected ClembleOAuthProviderToken readToken(String token) {
        return tokenRepository.findOne(token);
    }

    @Override
    protected void storeToken(String tokenValue, OAuthProviderTokenImpl token) {
        ClembleOAuthProviderToken providerToken = new ClembleOAuthProviderToken(tokenValue, token);
        tokenRepository.save(providerToken);
    }

    @Override
    protected ClembleOAuthProviderToken removeToken(String tokenValue) {
        ClembleOAuthProviderToken token = readToken(tokenValue);
        tokenRepository.delete(tokenValue);
        return token;
    }

}
