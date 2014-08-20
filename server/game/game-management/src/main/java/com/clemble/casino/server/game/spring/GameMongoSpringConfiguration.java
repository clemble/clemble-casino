package com.clemble.casino.server.game.spring;

import com.clemble.casino.server.game.repository.GameConstructionRepository;
import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.server.game.repository.GameScheduleRepository;
import com.clemble.casino.server.game.repository.ServerGameConfigurationRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import java.net.UnknownHostException;

/**
 * Created by mavarazy on 8/20/14.
 */
@Configuration
public class GameMongoSpringConfiguration implements SpringConfiguration {

    @Bean
    public MongoRepositoryFactory gameRepositoryFactory(@Value("${clemble.db.mongo.host}") String host, @Value("${clemble.db.mongo.port}") int port) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoOperations mongoOperations = new MongoTemplate(mongoClient, "clemble");
        return new MongoRepositoryFactory(mongoOperations);
    }

    @Bean
    public GameConstructionRepository gameConstructionRepository(MongoRepositoryFactory gameRepositoryFactory) {
        return gameRepositoryFactory.getRepository(GameConstructionRepository.class);
    }

    @Bean
    public GameRecordRepository gameRecordRepository(MongoRepositoryFactory gameRepositoryFactory) {
        return gameRepositoryFactory.getRepository(GameRecordRepository.class);
    }

    @Bean
    public GameScheduleRepository gameScheduleRepository(MongoRepositoryFactory gameRepositoryFactory) {
        return gameRepositoryFactory.getRepository(GameScheduleRepository.class);
    }

    @Bean
    public ServerGameConfigurationRepository serverGameConfigurationRepository(MongoRepositoryFactory gameRepositoryFactory) {
        return gameRepositoryFactory.getRepository(ServerGameConfigurationRepository.class);
    }

}
