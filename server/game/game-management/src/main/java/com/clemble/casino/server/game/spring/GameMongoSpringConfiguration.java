package com.clemble.casino.server.game.spring;

import com.clemble.casino.server.game.repository.GameRecordRepository;
import com.clemble.casino.server.game.repository.GameScheduleRepository;
import com.clemble.casino.server.spring.common.MongoSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

/**
 * Created by mavarazy on 8/20/14.
 */
@Configuration
@Import(MongoSpringConfiguration.class)
public class GameMongoSpringConfiguration implements SpringConfiguration {

    @Bean
    public GameRecordRepository gameRecordRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(GameRecordRepository.class);
    }

    @Bean
    public GameScheduleRepository gameScheduleRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(GameScheduleRepository.class);
    }

}
