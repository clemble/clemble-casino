package com.clemble.casino.server.game.construction.spring;

import com.clemble.casino.base.ActionLatchService;
import com.clemble.casino.base.JavaActionLatchService;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.server.game.construction.GameSessionKeyGenerator;
import com.clemble.casino.server.game.construction.controller.GameInitiationServiceController;
import com.clemble.casino.server.game.construction.listener.ServerGameInitiationDueEventListener;
import com.clemble.casino.server.game.construction.listener.ServerGameReadyEventListener;
import com.clemble.casino.server.game.construction.service.PendingGameInitiationService;
import com.clemble.casino.server.game.construction.service.ServerAutoGameConstructionService;
import com.clemble.casino.server.game.construction.listener.PendingGameInitiationEventListener;
import com.clemble.casino.server.game.construction.listener.PendingPlayerCreationEventListener;
import com.clemble.casino.server.game.construction.service.ServerAvailabilityGameConstructionService;
import com.clemble.casino.server.game.construction.controller.AutoGameConstructionController;
import com.clemble.casino.server.game.construction.controller.AvailabilityGameConstructionController;
import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import com.clemble.casino.server.game.construction.repository.PendingGameInitiationRepository;
import com.clemble.casino.server.game.construction.repository.PendingPlayerRepository;
import com.clemble.casino.server.game.construction.service.ServerGameInitiationService;
import com.clemble.casino.server.key.RedisKeyFactory;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.ServerNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import com.clemble.casino.server.player.notification.SystemNotificationServiceListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.spring.common.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.repository.GraphRepository;
import redis.clients.jedis.JedisPool;

/**
 * Created by mavarazy on 8/31/14.
 */
@Configuration
@Import({CommonSpringConfiguration.class,
    MongoSpringConfiguration.class,
    RedisSpringConfiguration.class,
    PresenceServiceSpringConfiguration.class,
    PaymentClientSpringConfiguration.class,
    GameConstructionSpringConfiguration.GameNeo4jSpringConfiguration.class})
public class GameConstructionSpringConfiguration {

    @Bean
    public GameSessionKeyGenerator gameSessionKeyGenerator(JedisPool jedisPool) {
        return new GameSessionKeyGenerator(new RedisKeyFactory("GAME_COUNTER", "G", jedisPool));
    }

    @Bean
    public ServerAutoGameConstructionService serverAutoGameConstructionService(
        final @Qualifier("gameSessionKeyGenerator") GameSessionKeyGenerator sessionKeyGenerator,
        final SystemNotificationService notificationService,
        final GameConstructionRepository constructionRepository,
        final PlayerLockService playerLockService,
        final ServerPlayerPresenceService playerStateManager,
        final @Qualifier("playerNotificationService") ServerNotificationService serverNotificationService,
        @Qualifier("playerAccountClient") PlayerAccountService accountServerService) {
        return new ServerAutoGameConstructionService(sessionKeyGenerator, notificationService, constructionRepository, playerLockService, playerStateManager, serverNotificationService, accountServerService);
    }

        @Bean
    public ActionLatchService constructionActionLatchService() {
        return new JavaActionLatchService();
    }

    @Bean
    public ServerAvailabilityGameConstructionService serverAvailabilityGameConstructionService(
        @Qualifier("constructionActionLatchService") ActionLatchService constructionActionLatchService,
        @Qualifier("gameSessionKeyGenerator") GameSessionKeyGenerator sessionKeyGenerator,
        @Qualifier("playerAccountClient") PlayerAccountService accountServerService,
        GameConstructionRepository constructionRepository,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        PendingGameInitiationService pendingInitiationService) {
        return new ServerAvailabilityGameConstructionService(constructionActionLatchService, sessionKeyGenerator, accountServerService, constructionRepository, notificationService, pendingInitiationService);
    }

    @Bean
    public PendingGameInitiationService pendingGameInitiationService(
        PendingPlayerRepository playerRepository,
        PendingGameInitiationRepository initiationRepository,
        ServerPlayerPresenceService presenceService,
        SystemNotificationService notificationService){
        return new PendingGameInitiationService(playerRepository, initiationRepository, presenceService, notificationService);
    }


    @Bean
    public PendingPlayerCreationEventListener pendingPlayerCreationEventListener(
        SystemNotificationServiceListener notificationServiceListener,
        PendingPlayerRepository playerRepository) {
        PendingPlayerCreationEventListener playerEventListener = new PendingPlayerCreationEventListener(playerRepository);
        notificationServiceListener.subscribe(playerEventListener);
        return playerEventListener;
    }

    @Bean
    public PendingGameInitiationEventListener pendingGameInitiationEventListener(
        PendingPlayerRepository playerRepository,
        PendingGameInitiationRepository initiationRepository,
        ServerPlayerPresenceService presenceService,
        SystemNotificationService notificationService,
        SystemNotificationServiceListener notificationServiceListener) {
        PendingGameInitiationEventListener eventListener = new PendingGameInitiationEventListener(playerRepository, initiationRepository, presenceService, notificationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public AutoGameConstructionController autoGameConstructionController(
        ServerAutoGameConstructionService constructionService) {
        return new AutoGameConstructionController(constructionService);
    }

    @Bean
    public AvailabilityGameConstructionController availabilityGameConstructionController(
        ServerAvailabilityGameConstructionService constructionService,
        GameConstructionRepository constructionRepository) {
        return new AvailabilityGameConstructionController(constructionService, constructionRepository);
    }

    @Bean
    public GameInitiationServiceController gameInitiationController(ServerGameInitiationService initiationService) {
        return new GameInitiationServiceController(initiationService);
    }

    @Bean
    public GameConstructionRepository gameConstructionRepository(MongoRepositoryFactory mongoRepositoryFactory) {
        return mongoRepositoryFactory.getRepository(GameConstructionRepository.class);
    }

       @Bean
    public ServerGameInitiationService serverGameInitiationActivator(
        PendingGameInitiationService processor,
        ServerPlayerPresenceService presenceService,
        @Qualifier("playerNotificationService") ServerNotificationService notificationService,
        SystemNotificationService systemNotificationService) {
        return new ServerGameInitiationService(processor, presenceService, notificationService, systemNotificationService);
    }

    @Bean
    public ServerGameReadyEventListener serverGameReadyEventListener(
        ServerGameInitiationService serverGameInitiationService,
        SystemNotificationServiceListener notificationServiceListener) {
        ServerGameReadyEventListener eventListener = new ServerGameReadyEventListener(serverGameInitiationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Bean
    public ServerGameInitiationDueEventListener serverGameInitiationDueEventListener(
        ServerGameInitiationService serverGameInitiationService,
        SystemNotificationServiceListener notificationServiceListener) {
        ServerGameInitiationDueEventListener eventListener = new ServerGameInitiationDueEventListener(serverGameInitiationService);
        notificationServiceListener.subscribe(eventListener);
        return eventListener;
    }

    @Configuration
    @EnableNeo4jRepositories(basePackages = "com.clemble.casino.server.game.construction.repository", includeFilters = { @ComponentScan.Filter(value = GraphRepository.class, type = FilterType.ASSIGNABLE_TYPE) })
    public static class GameNeo4jSpringConfiguration extends BasicNeo4JSpringConfiguration {

        public GameNeo4jSpringConfiguration(){
            setBasePackage("com.clemble.casino.server.player.social", "com.clemble.casino.server.game.construction");
        }

        @Override
        public String getFolder() {
            return "target/game_graph";
        }

    }

}
