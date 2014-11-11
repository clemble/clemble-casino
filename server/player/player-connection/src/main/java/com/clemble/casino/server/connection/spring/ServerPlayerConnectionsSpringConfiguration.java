package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.connection.MongoPlayerGraph;
import com.clemble.casino.server.connection.NeoPlayerGraph;
import com.clemble.casino.server.connection.repository.NeoPlayerGraphRepository;
import com.clemble.casino.server.connection.repository.MongoPlayerGraphRepository;
import com.clemble.casino.server.connection.service.PlayerGraphService;
import com.clemble.casino.server.connection.service.SimplePlayerGraphService;
import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.util.Collections;

/**
 * Created by mavarazy on 8/12/14.
 */
public class ServerPlayerConnectionsSpringConfiguration {

    @Configuration
    @EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.connection.repository", includeFilters = { @ComponentScan.Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
    public static class GraphPlayerConnectionsSpringConfigurations extends BasicNeo4JSpringConfiguration {

        public GraphPlayerConnectionsSpringConfigurations() {
            setBasePackage("com.clemble.casino.server.connection", "com.clemble.casino.server.game.pending");
        }

        @Override
        public String getFolder() {
            return "target/player_graph";
        }

        @Bean
        public SimplePlayerGraphService connectionService(NeoPlayerGraphRepository connectionRepository) {
            return new SimplePlayerGraphService<NeoPlayerGraph, Long>((player) -> new NeoPlayerGraph(player),connectionRepository);
        }

    }

    @Configuration
    @Import(MongoSpringConfiguration.class)
    public static class MongoPlayerConnectionsSpringConfiguration implements SpringConfiguration {

        @Bean
        public MongoPlayerGraphRepository mongoPlayerConnectionsRepository(MongoRepositoryFactory mongoRepositoryFactory) {
            return mongoRepositoryFactory.getRepository(MongoPlayerGraphRepository.class);
        }

        @Bean
        public PlayerGraphService playerGraphService(MongoPlayerGraphRepository mongoPlayerConnectionsRepository) {
            return new SimplePlayerGraphService<MongoPlayerGraph, String>((player) -> new MongoPlayerGraph(player, Collections.emptySet(), Collections.emptySet()), mongoPlayerConnectionsRepository);
        }

    }

}
