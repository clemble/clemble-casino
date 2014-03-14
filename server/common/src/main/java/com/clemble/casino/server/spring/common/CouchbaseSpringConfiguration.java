package com.clemble.casino.server.spring.common;

import java.net.URI;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.CouchbaseClient;

@Configuration
public class CouchbaseSpringConfiguration implements SpringConfiguration {

    @Bean(destroyMethod = "shutdown")
    public CouchbaseClient couchbaseClient(
        @Value("${clemble.db.player.couchbase.url}") String url,
        @Value("${clemble.db.player.couchbase.bucket}") String bucket,
        @Value("${clemble.db.player.couchbase.password}") String password) throws Exception {
        return new CouchbaseClient(Arrays.asList(new URI(url)), bucket, password);
    }


}
