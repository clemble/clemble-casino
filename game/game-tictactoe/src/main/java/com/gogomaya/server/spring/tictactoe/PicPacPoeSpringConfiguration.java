package com.gogomaya.server.spring.tictactoe;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.gogomaya.server.game.Game;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.game.action.GameTableFactory;
import com.gogomaya.server.game.aspect.bet.GameBetAspectFactory;
import com.gogomaya.server.game.aspect.outcome.GameOutcomeAspectFactory;
import com.gogomaya.server.game.aspect.price.GamePriceAspectFactory;
import com.gogomaya.server.game.aspect.security.GameSecurityAspectFactory;
import com.gogomaya.server.game.aspect.time.GameTimeAspectFactory;
import com.gogomaya.server.game.cache.GameCacheService;
import com.gogomaya.server.game.configuration.GameSpecificationConfigurationManager;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.construct.GameInitiatorService;
import com.gogomaya.server.game.construct.SimpleGameConstructionService;
import com.gogomaya.server.game.construct.SimpleGameInitiatorService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.player.account.PlayerAccountService;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.repository.game.GameSpecificationRepository;
import com.gogomaya.server.repository.game.GameTableRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;
import com.gogomaya.server.tictactoe.PicPacPoeState;
import com.gogomaya.server.tictactoe.PicPacPoeStateFactory;

@Configuration
@Import(value = { GameManagementSpringConfiguration.class })
public class PicPacPoeSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("gameSessionRepository")
    public GameSessionRepository<PicPacPoeState> gameSessionRepository;

    @Autowired
    @Qualifier("gameSpecificationRepository")
    public GameSpecificationRepository gameSpecificationRepository;

    @Autowired
    @Qualifier("gameTableRepository")
    public GameTableRepository<PicPacPoeState> gameTableRepository;

    @Autowired(required = true)
    @Qualifier("gameConstructionRepository")
    public GameConstructionRepository gameConstructionRepository;

    @Autowired
    @Qualifier("playerNotificationService")
    public PlayerNotificationService playerNotificationService;

    @Autowired
    @Qualifier("gamePriceAspectFactory")
    public GamePriceAspectFactory gamePriceAspectFactory;

    @Autowired
    @Qualifier("gameBetAspectFactory")
    public GameBetAspectFactory gameBetAspectFactory;

    @Autowired
    @Qualifier("gameSecurityAspectFactory")
    public GameSecurityAspectFactory gameSecurityAspectFactory;

    @Autowired
    @Qualifier("gameOutcomeAspectFactory")
    public GameOutcomeAspectFactory gameOutcomeAspectFactory;

    @Autowired
    @Qualifier("gameTimeAspectFactory")
    public GameTimeAspectFactory gameTimeAspectFactory;

    @Autowired
    @Qualifier("playerAccountService")
    public PlayerAccountService playerAccountService;

    @Autowired
    @Qualifier("tableServerRegistry")
    private TableServerRegistry tableServerRegistry;

    @Autowired
    @Qualifier("playerStateManager")
    public PlayerStateManager playerStateManager;

    @Autowired
    @Qualifier("playerLockService")
    public PlayerLockService playerLockService;

    @Bean
    @Singleton
    public GameTableFactory<PicPacPoeState> picPacPoeTableFactory() {
        return new GameTableFactory<PicPacPoeState>(ticTacToeStateFactory(), gameTableRepository, tableServerRegistry);
    }

    @Bean
    @Singleton
    public GameConstructionService picPacPoeConstructionService() {
        return new SimpleGameConstructionService(playerAccountService, playerNotificationService, gameConstructionRepository, picPacPoeInitiatorService(),
                playerLockService, playerStateManager);
    }

    @Bean
    @Singleton
    public GameInitiatorService picPacPoeInitiatorService() {
        return new SimpleGameInitiatorService(picPacPoeSessionProcessor(), playerStateManager);
    }

    @Bean
    @Singleton
    public PicPacPoeStateFactory ticTacToeStateFactory() {
        return new PicPacPoeStateFactory(gameConstructionRepository, picPacPoeProcessorFactory());
    }

    @Bean
    @Singleton
    public GameSpecificationConfigurationManager picPacPoeConfigurationManager() {
        return new GameSpecificationConfigurationManager(Game.pic, gameSpecificationRepository);
    }

    @Bean
    @Singleton()
    public GameProcessorFactory<PicPacPoeState> picPacPoeProcessorFactory() {
        return new GameProcessorFactory<PicPacPoeState>(gameSecurityAspectFactory, gameBetAspectFactory, gamePriceAspectFactory, gameTimeAspectFactory,
                gameOutcomeAspectFactory);
    }

    @Bean
    @Singleton
    public GameCacheService<PicPacPoeState> picPacPoeCacheService() {
        return new GameCacheService<PicPacPoeState>(gameConstructionRepository, gameSessionRepository, picPacPoeProcessorFactory(), ticTacToeStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<PicPacPoeState> picPacPoeSessionProcessor() {
        return new GameSessionProcessor<PicPacPoeState>(Game.pic, picPacPoeTableFactory(), picPacPoeCacheService(), playerNotificationService);
    }

}
