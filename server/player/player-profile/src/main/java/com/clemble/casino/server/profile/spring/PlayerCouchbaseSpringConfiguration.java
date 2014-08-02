package com.clemble.casino.server.profile.spring;

import com.clemble.casino.server.profile.repository.CouchbasePlayerProfileRepository;
import com.clemble.casino.server.profile.repository.PlayerProfileRepository;
import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.util.Arrays;

/**
 * Created by mavarazy on 6/12/14.
 */
@Configuration
public class PlayerCouchbaseSpringConfiguration {

    @Bean
    public PlayerProfileRepository playerProfileRepository(ObjectMapper objectMapper, CouchbaseClient playerCouchbaseClient) {
        return new CouchbasePlayerProfileRepository(playerCouchbaseClient, objectMapper);
    }

    @Bean(destroyMethod = "shutdown")
    public CouchbaseClient playerCouchbaseClient(
        @Value("${clemble.db.player.couchbase.url}") String url,
        @Value("${clemble.db.player.couchbase.bucket}") String bucket,
        @Value("${clemble.db.player.couchbase.password}") String password) throws Exception {
        return new CouchbaseClient(Arrays.asList(new URI(url)), bucket, password);
    }

}
