package com.clemble.casino.server.spring.web;

import com.clemble.casino.server.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.spring.common.JsonSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = {
    OAuthSpringConfiguration.SimpleOAuthSpringConfiguration.class
})
public class OAuthSpringConfiguration implements SpringConfiguration {

    @Configuration
    public static class SimpleOAuthSpringConfiguration implements SpringConfiguration {

        @Bean
        public ClembleConsumerDetailsService clembleConsumerDetailsService() {
            return new SimpleClembleConsumerDetailsService();
        }

    }

//    @Configuration
//    @Import(value = { SecurityCouchbaseSpringConfiguration.class, JsonSpringConfiguration.class })
//    public static class CouchbaseOAuthSpringConfiguration implements SpringConfiguration {
//
//        @Autowired
//        public CouchbaseClient securityCouchbaseClient;
//
//        @Autowired
//        public TokenRepository tokenRepository;
//
//        @Autowired
//        @Qualifier("objectMapper")
//        public ObjectMapper objectMapper;
//
//        @Bean
//        public ClembleOAuthProviderTokenServices clembleOAuthProviderTokenService() {
//            return new ClembleOAuthProviderTokenServices(tokenRepository);
//        }
//
//        @Bean
//        public ClembleCouchbaseConsumerDetailsService clembleConsumerDetailsService() {
//            return new ClembleCouchbaseConsumerDetailsService(securityCouchbaseClient, objectMapper);
//        }
//
//    }
}
