package com.clemble.casino.server.connection.spring;

import com.clemble.casino.server.connection.repository.GraphPlayerConnectionsRepository;
import com.clemble.casino.server.connection.repository.MongoPlayerConnectionsRepository;
import com.clemble.casino.server.connection.service.GraphPlayerConnectionService;
import com.clemble.casino.server.connection.service.MongoPlayerConnectionsService;
import com.clemble.casino.server.spring.common.BasicNeo4JSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;

import java.net.UnknownHostException;

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
    public static class MongoPlayerConnectionsSpringConfiguration implements SpringConfiguration {

        @Bean
        public MongoRepositoryFactory paymentRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
            MongoClient mongoClient = new MongoClient(host, port);
            MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
            return new MongoRepositoryFactory(mongoOperations);
        }

        @Bean
        public MongoPlayerConnectionsRepository mongoPlayerConnectionsRepository(@Qualifier("paymentRepositoryFactory") MongoRepositoryFactory paymentRepositoryFactory) {
            return paymentRepositoryFactory.getRepository(MongoPlayerConnectionsRepository.class);
        }

        @Bean
        public MongoPlayerConnectionsService playerConnectionsService(MongoPlayerConnectionsRepository mongoPlayerConnectionsRepository) {
            return new MongoPlayerConnectionsService(mongoPlayerConnectionsRepository);
        }

    }

}
