package com.clemble.casino.server.security;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth.provider.token.OAuthProviderToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.server.spring.OAuthSpringConfiguration;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OAuthSpringConfiguration.class)
public class ClembleOAuthProviderTokerServiceTest {

    @Autowired
    public ClembleOAuthProviderTokenServices providerTokenServices;

    @Test
    public void initialize() {
    }

    @Test
    public void generate() {
        OAuthProviderToken oAuthProviderToken = providerTokenServices.createUnauthorizedRequestToken(RandomStringUtils.random(5), null);
        OAuthProviderToken readOAuthProviderToken = providerTokenServices.getToken(oAuthProviderToken.getValue());
        assertEquals(readOAuthProviderToken.getCallbackUrl(), oAuthProviderToken.getCallbackUrl());
        assertEquals(readOAuthProviderToken.getConsumerKey(), oAuthProviderToken.getConsumerKey());
        assertEquals(readOAuthProviderToken.getSecret(), oAuthProviderToken.getSecret());
        assertEquals(readOAuthProviderToken.getValue(), oAuthProviderToken.getValue());
        assertEquals(readOAuthProviderToken.getVerifier(), oAuthProviderToken.getVerifier());
    }
}
