package com.clemble.casino.server.security;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;

/**
 * Created by mavarazy on 6/14/14.
 */
public interface MongoTokenRepository extends MongoRepository<ClembleOAuthProviderToken, String> {
}
