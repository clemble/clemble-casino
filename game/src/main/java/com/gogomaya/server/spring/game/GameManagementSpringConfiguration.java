package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.game.connection.GameServerConnectionManager;
import com.gogomaya.server.game.connection.GameNotificationManager;
import com.gogomaya.server.game.match.GameStateManager;
import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.game.table.GameTableRepository;

@Configuration
@Import(value = { GameJPASpringConfiguration.class })
public class GameManagementSpringConfiguration {

    @Inject
    public GameSessionRepository sessionRepository;

    @Inject
    public RedisTemplate<byte[], Long> redisTemplate;

    @Inject
    public GameTableRepository tableRepository;

    @Inject
    public GameServerConnectionManager serverConnectionManager;

    @Inject
    public JsonMessageConverter jsonMessageConverter;

    @Bean
    @Singleton
    public GameNotificationManager notificationManager() {
        return new GameNotificationManager(jsonMessageConverter);
    }

    @Bean
    @Singleton
    public GameTableManager tableManager() {
        return new GameTableManager(redisTemplate, tableRepository, serverConnectionManager);
    }

    @Bean
    @Singleton
    public GameStateManager stateManager() {
        return new GameStateManager(tableManager(), tableRepository, sessionRepository, notificationManager());
    }

}
