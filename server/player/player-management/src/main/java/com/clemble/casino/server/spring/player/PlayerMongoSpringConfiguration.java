package com.clemble.casino.server.spring.player;

import com.clemble.casino.server.repository.player.MongoPlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 6/12/14.
 */
@Configuration
public class PlayerMongoSpringConfiguration {

    @Bean
    public PlayerProfileRepository playerProfileRepository(@Value("${clemble.db.redis.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "player");
        MongoRepositoryFactory repositoryFactory = new MongoRepositoryFactory(mongoOperations);
        return repositoryFactory.getRepository(MongoPlayerProfileRepository.class);
    }

}
