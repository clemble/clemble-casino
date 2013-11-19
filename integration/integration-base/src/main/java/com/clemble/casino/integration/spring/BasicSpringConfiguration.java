package com.clemble.casino.integration.spring;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.integration.game.GameSessionPlayerFactory;
import com.clemble.casino.integration.game.SimpleGameSessionPlayerFactory;
import com.clemble.casino.integration.game.construction.SimpleGameScenarios;
import com.clemble.casino.integration.payment.PaymentServiceFactory;
import com.clemble.casino.integration.player.PlayerOperations;
import com.clemble.casino.integration.player.SimplePlayerOperations;
import com.clemble.casino.integration.player.profile.PlayerProfileServiceFactory;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class BasicSpringConfiguration {

    @Autowired
    @Qualifier("playerOperations")
    public PlayerOperations playerOperations;

    @Autowired
    public GameSessionPlayerFactory sessionPlayerFactory;

    @PostConstruct
    public void initialize() {
        ObjectGenerator.register(RSAKeySecret.class, new AbstractValueGenerator<RSAKeySecret>() {

            @Override
            public RSAKeySecret generate() {
                try {
                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                    generator.initialize(1024);
                    KeyPair keyPair = generator.generateKeyPair();
                    return new RSAKeySecret(keyPair.getPrivate(), keyPair.getPublic());
                } catch (NoSuchAlgorithmException algorithmException) {
                    return null;
                }
            }
        });
    }

    @Bean
    @Singleton
    public SimpleGameScenarios gameScenarios() {
        return new SimpleGameScenarios(playerOperations);
    }

    @Bean
    @Autowired
    public GameSessionPlayerFactory<GameState> sessionPlayerFactory() {
        return new SimpleGameSessionPlayerFactory<GameState>();
    }

    @Bean
    public EventListenerOperationsFactory playerListenerOperations() {
        if (new Random().nextBoolean()) {
            return new EventListenerOperationsFactory.RabbitEventListenerServiceFactory();
        } else {
            return new EventListenerOperationsFactory.StompEventListenerServiceFactory();
        }
    }
    
    @Bean
    @Autowired
    public PlayerOperations playerOperations(ObjectMapper objectMapper,
            EventListenerOperationsFactory listenerOperations,
            PlayerRegistrationService registrationService,
            PlayerProfileServiceFactory profileOperations,
            PlayerSessionService sessionOperations,
            PaymentServiceFactory accountOperations) {
        return new SimplePlayerOperations(objectMapper, listenerOperations, registrationService, profileOperations, sessionOperations, accountOperations);
    }


}
