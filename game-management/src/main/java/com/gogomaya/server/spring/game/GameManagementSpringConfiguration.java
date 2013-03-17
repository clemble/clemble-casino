package com.gogomaya.server.spring.game;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import com.gogomaya.server.game.match.GameMatchingService;
import com.gogomaya.server.game.session.GameSessionRepository;

@Configuration
@Import(value = { GameJPASpringConfiguration.class })
public class GameManagementSpringConfiguration {

    @Inject
    public GameSessionRepository sessionRepository;

    @Inject
    public RedisTemplate<byte[], Long> redisTemplate;

    @Bean
    @Singleton
    public GameMatchingService gameMatchingService() {
        return new GameMatchingService(redisTemplate, sessionRepository);
    }

}
