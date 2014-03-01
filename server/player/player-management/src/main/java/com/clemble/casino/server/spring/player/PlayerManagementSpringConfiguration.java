package com.clemble.casino.server.spring.player;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.repository.player.CouchbasePlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.CouchbaseSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, CouchbaseSpringConfiguration.class, PlayerNeo4JSpringConfiguration.class })
public class PlayerManagementSpringConfiguration implements SpringConfiguration {

    @Bean
    public PlayerProfileRepository playerProfileRepository(ObjectMapper objectMapper, CouchbaseClient playerCouchbaseClient) {
        return new CouchbasePlayerProfileRepository(playerCouchbaseClient, objectMapper);
    }

}
