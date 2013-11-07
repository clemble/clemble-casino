package com.clemble.casino.server.security;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;

public class ClembleCasinoSecurityFilter extends ProtectedResourceProcessingFilter {

    public ClembleCasinoSecurityFilter(OAuthProviderTokenServices tokenServices, ConsumerDetailsService consumerDetailsService) {
        setTokenServices(checkNotNull(tokenServices));
        setConsumerDetailsService(checkNotNull(consumerDetailsService));
        setIgnoreMissingCredentials(false);
        setNonceServices(new ClembleOAuthNonceService(60));
    }

    @Override
    protected boolean skipProcessing(HttpServletRequest request) {
        return false;
    }
}
