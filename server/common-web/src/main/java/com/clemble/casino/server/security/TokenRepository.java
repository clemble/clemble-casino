package com.clemble.casino.server.security;

/**
 * Created by mavarazy on 6/14/14.
 */
public interface TokenRepository {

    public ClembleOAuthProviderToken findOne(String token);

    public void save(ClembleOAuthProviderToken token);

    public void delete(String token);

}
