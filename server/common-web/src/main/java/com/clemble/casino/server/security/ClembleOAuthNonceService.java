package com.clemble.casino.server.security;

import java.util.concurrent.TimeUnit;

import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth.provider.ConsumerDetails;
import org.springframework.security.oauth.provider.nonce.OAuthNonceServices;

public class ClembleOAuthNonceService implements OAuthNonceServices {

    final private long maxTimeout;

    public ClembleOAuthNonceService(long maxTimeoutSec) {
        this.maxTimeout = TimeUnit.MINUTES.toMillis(Math.max(maxTimeoutSec, 90));
    }

    @Override
    public void validateNonce(ConsumerDetails consumerDetails, long timestamp, String nonce) throws AuthenticationException {
        if (System.currentTimeMillis() - timestamp > maxTimeout)
            throw new CredentialsExpiredException("Expired timestamp.");
    }

}
