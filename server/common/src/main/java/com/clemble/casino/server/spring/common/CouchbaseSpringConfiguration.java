package com.clemble.casino.server.spring.common;

import java.net.URI;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import com.couchbase.client.CouchbaseClient;

@Configuration
public class CouchbaseSpringConfiguration implements SpringConfiguration {

    @Bean
    public CouchbaseClient couchbaseClient() throws Exception {
        return new CouchbaseClient(Arrays.asList(new URI("http://localhost:8091/pools")), "player", "");
    }

    @Bean
    public CouchbaseTemplate couchbaseTemplate() throws Exception {
        return new CouchbaseTemplate(couchbaseClient());
    }

}
