package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.connection.repository.GraphPlayerConnectionsRepository;
import com.clemble.casino.server.connection.repository.MongoPlayerConnectionsRepository;
import com.clemble.casino.server.connection.service.GraphPlayerConnectionService;
import com.clemble.casino.server.connection.service.MongoPlayerConnectionsService;
import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

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
        public GraphPlayerConnectionService connectionService(GraphPlayerConnectionsRepository connectionRepository) {
            return new GraphPlayerConnectionService(connectionRepository);
        }

    }

    @Configuration
    @Import(MongoSpringConfiguration.class)
    public static class MongoPlayerConnectionsSpringConfiguration implements SpringConfiguration {

        @Bean
        public MongoPlayerConnectionsRepository mongoPlayerConnectionsRepository(MongoRepositoryFactory mongoRepositoryFactory) {
            return mongoRepositoryFactory.getRepository(MongoPlayerConnectionsRepository.class);
        }

        @Bean
        public MongoPlayerConnectionsService playerConnectionsService(MongoPlayerConnectionsRepository mongoPlayerConnectionsRepository) {
            return new MongoPlayerConnectionsService(mongoPlayerConnectionsRepository);
        }

    }

}
