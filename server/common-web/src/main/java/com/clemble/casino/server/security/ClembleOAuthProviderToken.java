package com.clemble.casino.server.security;

import org.springframework.data.annotation.Id;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenImpl;

/**
 * Created by mavarazy on 6/14/14.
 */
public class ClembleOAuthProviderToken extends OAuthProviderTokenImpl {

    @Id
    private String token;

    public ClembleOAuthProviderToken() {
    }

    public ClembleOAuthProviderToken(String token, OAuthProviderTokenImpl providerToken) {
        this.token = token;
        setCallbackUrl(providerToken.getCallbackUrl());
        setConsumerKey(providerToken.getConsumerKey());
        setSecret(providerToken.getSecret());
        setTimestamp(providerToken.getTimestamp());
        setUserAuthentication(providerToken.getUserAuthentication());
        setValue(providerToken.getValue());
        setVerifier(providerToken.getVerifier());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClembleOAuthProviderToken that = (ClembleOAuthProviderToken) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }
}
