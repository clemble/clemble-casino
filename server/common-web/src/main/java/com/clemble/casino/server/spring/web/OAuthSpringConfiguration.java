package com.clemble.casino.server.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.security.ClembleConsumerDetailsService;
import com.clemble.casino.server.security.ClembleOAuthProviderTokenServices;
import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = { SecurityCouchbaseSpringConfiguration.class, JsonSpringConfiguration.class })
public class OAuthSpringConfiguration implements SpringConfiguration {

    @Autowired
    public CouchbaseClient securityCouchbaseClient;

    @Autowired
    @Qualifier("objectMapper")
    public ObjectMapper objectMapper;

    @Bean
    public ClembleOAuthProviderTokenServices clembleOAuthProviderTokenService() {
        return new ClembleOAuthProviderTokenServices(securityCouchbaseClient, objectMapper);
    }

    @Bean
    public ClembleConsumerDetailsService clembleConsumerDetailsService() {
        return new ClembleConsumerDetailsService(securityCouchbaseClient, objectMapper);
    }

}
