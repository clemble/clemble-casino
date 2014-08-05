package com.clemble.casino.integration.spring;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;

import com.clemble.casino.player.service.*;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import com.clemble.casino.server.goal.controller.GoalServiceController;
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;
import com.clemble.casino.server.payment.controller.PlayerAccountServiceController;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.AvailabilityGameConstructionService;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.AutoGameConstructionService;
import com.clemble.casino.game.service.GameInitiationService;
import com.clemble.casino.game.service.GameConfigurationService;
import com.clemble.casino.game.service.GameRecordService;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.integration.game.GamePlayerFactory;
import com.clemble.casino.integration.game.SimpleRoundGamePlayerFactory;
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
import com.clemble.casino.payment.service.PaymentTransactionServiceContract;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;
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
    public SimpleGameScenarios gameScenarios(GamePlayerFactory gamePlayerFactory) {
        return new SimpleGameScenarios(playerOperations, gamePlayerFactory);
    }

    @Bean
    public GamePlayerFactory gamePlayerFactory(){
        return new GamePlayerFactory();
    }

    @Bean
    public SyncGameScenarios syncGameScenarios(GameScenarios gameScenarios) {
        return new SimpleSyncGameScenarios(gameScenarios);
    }

    @Bean
    public SimpleRoundGamePlayerFactory<? extends GameState> sessionPlayerFactory() {
        return new SimpleRoundGamePlayerFactory<GameState>();
    }

    @Bean
    public PlayerScenarios playerScenarios(ClembleCasinoRegistrationOperations registrationOperations) {
        return new SimplePlayerScenarios(registrationOperations);
    }

    @Configuration
    @Profile({TEST, DEFAULT})
    public static class Test {

        @Bean
        public EventListenerOperationsFactory playerListenerOperations() {
            return new EventListenerOperationsFactory.RabbitEventListenerServiceFactory();
        }

        @Bean
        public PaymentTransactionOperations paymentTransactionOperations(PaymentTransactionServiceContract paymentTransactionController, SystemNotificationService systemNotificationService) {
            return new WebPaymentTransactionOperations(paymentTransactionController, systemNotificationService);
        }

        @Bean
        public ClembleCasinoRegistrationOperations registrationOperations(
            @Value("${clemble.host}") String host,
            ObjectMapper objectMapper,
            EventListenerOperationsFactory listenerOperations,
            PlayerFacadeRegistrationService registrationService,
            PlayerProfileService profileOperations,
            PlayerImageService imageService,
            PlayerConnectionServiceController connectionService,
            PlayerSessionService sessionOperations,
            @Qualifier("playerAccountController") PlayerAccountServiceController accountOperations,
            PaymentTransactionServiceController paymentTransactionService,
            PlayerPresenceService presenceService,
            @Qualifier("autoGameConstructionController") AutoGameConstructionService constructionService,
            @Qualifier("availabilityGameConstructionController") AvailabilityGameConstructionService availabilityConstructionService,
            @Qualifier("gameInitiationController") GameInitiationService initiationService,
            @Qualifier("gameConfigurationController") GameConfigurationService specificationService,
            GameActionService actionService,
            GameRecordService recordService,
            GoalServiceController goalService) {
            ClembleCasinoRegistrationOperations registrationOperations = new ServerClembleCasinoRegistrationOperations(host, objectMapper, listenerOperations, registrationService, profileOperations, imageService, connectionService, sessionOperations, accountOperations, paymentTransactionService, presenceService, constructionService, availabilityConstructionService, initiationService, specificationService, actionService, recordService, goalService);
            return new ClembleCasinoRegistrationOperationsWrapper(registrationOperations);
        }
    }
}
