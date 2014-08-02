package com.clemble.casino.server.registration.spring;

import com.clemble.casino.server.registration.security.ClembleConsumerDetailsService;
import com.clemble.casino.server.registration.security.SimpleClembleConsumerDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.spring.common.SpringConfiguration;

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
