package com.clemble.casino.server.spring.player;

import java.net.URI;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.couchbase.client.CouchbaseClient;

@Configuration
public class PlayerCouchbaseSpringConfiguration implements SpringConfiguration {

    @Bean(destroyMethod = "shutdown")
    public CouchbaseClient playerCouchbaseClient() throws Exception {
        return new CouchbaseClient(Arrays.asList(new URI("http://localhost:8091/pools")), "player", "");
    }

}
