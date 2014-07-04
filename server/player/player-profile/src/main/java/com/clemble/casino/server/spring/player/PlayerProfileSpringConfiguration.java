package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.repository.player.MongoPlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.web.player.PlayerProfileController;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 7/4/14.
 */
@Configuration
public class PlayerProfileSpringConfiguration implements SpringConfiguration {

    @Bean
    public MongoRepositoryFactory mongoRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "player");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public PlayerProfileRepository playerProfileRepository(MongoRepositoryFactory repositoryFactory) {
        return repositoryFactory.getRepository(MongoPlayerProfileRepository.class);
    }

    @Bean
    public PlayerProfileController playerProfileController(PlayerProfileRepository playerProfileRepository) {
        return new PlayerProfileController(playerProfileRepository);
    }

}
