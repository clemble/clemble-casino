package com.clemble.casino.server.spring.common;

import java.net.URI;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.CouchbaseClient;

@Configuration
public class CouchbaseSpringConfiguration implements SpringConfiguration {

    @Bean
    public CouchbaseClient couchbaseClient() throws Exception {
        return new CouchbaseClient(Arrays.asList(new URI("http://localhost:8091/pools")), "player", "");
    }

}
