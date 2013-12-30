package com.clemble.casino.server.spring.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.clemble.casino.server.repository.player.CouchbasePlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.spring.common.CommonSpringConfiguration;
import com.clemble.casino.server.spring.common.CouchbaseSpringConfiguration;
import com.clemble.casino.server.spring.common.Neo4JSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.couchbase.client.CouchbaseClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = { CommonSpringConfiguration.class, PlayerCommonSpringConfiguration.class, CouchbaseSpringConfiguration.class, Neo4JSpringConfiguration.class })
public class PlayerManagementSpringConfiguration implements SpringConfiguration {

    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    public CouchbaseClient playerCouchbaseClient;

    @Bean
    public PlayerProfileRepository playerProfileRepository() {
        return new CouchbasePlayerProfileRepository(playerCouchbaseClient, objectMapper);
    }
}
