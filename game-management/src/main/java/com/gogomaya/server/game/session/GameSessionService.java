package com.gogomaya.server.game.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.gogomaya.server.game.rule.GameRuleSpecification;

@Service
public class GameSessionService {
    
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public GameSession create(long userId, GameRuleSpecification ruleSpecification) {
        return null;
    }

}
