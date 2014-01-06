package com.clemble.casino.integration.spring;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.game.service.GameSpecificationService;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.integration.game.SimpleGameSessionPlayerFactory;
import com.clemble.casino.integration.game.construction.GameScenarios;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.game.construction.SimpleGameScenarios;
import com.clemble.casino.integration.game.construction.SimplePlayerScenarios;
import com.clemble.casino.integration.game.construction.SimpleSyncGameScenarios;
import com.clemble.casino.integration.game.construction.SyncGameScenarios;
import com.clemble.casino.integration.payment.PaymentTransactionOperations;
import com.clemble.casino.integration.payment.WebPaymentTransactionOperations;
import com.clemble.casino.integration.player.ClembleCasinoRegistrationOperationsWrapper;
import com.clemble.casino.integration.player.ServerClembleCasinoRegistrationOperations;
import com.clemble.casino.integration.player.account.CombinedPaymentService;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.server.payment.ServerPaymentTransactionService;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
import com.clemble.test.spring.listener.TestContextListenerRegistrator;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@Import(value = {BaseTestSpringConfiguration.Test.class})
public class BaseTestSpringConfiguration implements TestSpringConfiguration {

    @Autowired
    public PlayerScenarios playerOperations;

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
    public SimpleGameScenarios gameScenarios() {
        return new SimpleGameScenarios(playerOperations);
    }

    @Bean
    public SyncGameScenarios syncGameScenarios(GameScenarios gameScenarios) {
        return new SimpleSyncGameScenarios(gameScenarios);
    }

    @Bean
    public SimpleGameSessionPlayerFactory<? extends GameState> sessionPlayerFactory() {
        return new SimpleGameSessionPlayerFactory<GameState>();
    }

    @Bean
    public PlayerScenarios playerScenarios(ClembleCasinoRegistrationOperations registrationOperations) {
        return new SimplePlayerScenarios(registrationOperations);
    }


    @Configuration
    @Profile({UNIT_TEST, DEFAULT})
    public static class Test {

        @Bean
        public EventListenerOperationsFactory playerListenerOperations() {
//            if (new Random().nextBoolean()) {
                return new EventListenerOperationsFactory.RabbitEventListenerServiceFactory();
//            } else {
//                return new EventListenerOperationsFactory.StompEventListenerServiceFactory();
//            }
        }

        @Bean
        public PaymentService paymentService(@Qualifier("paymentTransactionController") PaymentTransactionService transactionService, PlayerAccountService accountService) {
            return new CombinedPaymentService(transactionService, accountService);
        }

        @Bean
        public PaymentTransactionOperations paymentTransactionOperations(ServerPaymentTransactionService paymentTransactionController) {
            return new WebPaymentTransactionOperations(paymentTransactionController);
        }

        @Bean
        public TestContextListenerRegistrator testContextListener(){
            return new TestContextListenerRegistrator();
        }

        @Bean
        public ClembleCasinoRegistrationOperations registrationOperations(ObjectMapper objectMapper,
                EventListenerOperationsFactory listenerOperations,
                PlayerRegistrationService registrationService,
                PlayerProfileService profileOperations,
                PlayerConnectionService connectionService,
                PlayerSessionService sessionOperations,
                PaymentService accountOperations,
                PlayerPresenceService presenceService,
                GameConstructionService constructionService,
                GameSpecificationService specificationService,
                GameActionService<?> actionService) {
            ClembleCasinoRegistrationOperations registrationOperations = new ServerClembleCasinoRegistrationOperations(objectMapper, listenerOperations, registrationService, profileOperations, connectionService, sessionOperations, accountOperations, presenceService, constructionService, specificationService, actionService);
            return new ClembleCasinoRegistrationOperationsWrapper(registrationOperations);
        }
        
    }

}
