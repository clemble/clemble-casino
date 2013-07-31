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
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.construct.GameInitiatorService;
import com.gogomaya.server.game.construct.SimpleGameConstructionService;
import com.gogomaya.server.game.construct.SimpleGameInitiatorService;
import com.gogomaya.server.game.notification.TableServerRegistry;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
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
import com.gogomaya.server.tictactoe.action.impl.TicTacToeProcessor;
import com.gogomaya.server.tictactoe.action.impl.TicTacToeStateFactory;
import com.gogomaya.server.tictactoe.configuration.TicTacToeConfigurationManager;

@Configuration
@Import(value = { GameManagementSpringConfiguration.class })
public class TicTacToeSpringConfiguration implements SpringConfiguration {

    @Autowired
    @Qualifier("gameSessionRepository")
    public GameSessionRepository<TicTacToeState> gameSessionRepository;

    @Autowired
    @Qualifier("gameSpecificationRepository")
    public GameSpecificationRepository gameSpecificationRepository;

    @Autowired
    @Qualifier("gameTableRepository")
    public GameTableRepository<TicTacToeState> gameTableRepository;

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
    public GameTableFactory<TicTacToeState> ticTacToeTableFactory() {
        return new GameTableFactory<TicTacToeState>(ticTacToeStateFactory(), gameTableRepository, tableServerRegistry);
    }

    @Bean
    @Singleton
    public GameConstructionService ticTacToeConstructionService() {
        return new SimpleGameConstructionService(playerAccountService, playerNotificationService, gameConstructionRepository, ticTacToeInitiatorService(),
                playerLockService, playerStateManager);
    }

    @Bean
    @Singleton
    public GameInitiatorService ticTacToeInitiatorService() {
        return new SimpleGameInitiatorService(ticTacToeSessionProcessor(), playerStateManager);
    }

    @Bean
    @Singleton
    public TicTacToeStateFactory ticTacToeStateFactory() {
        return new TicTacToeStateFactory(gameConstructionRepository, ticTacToeProcessorFactory());
    }

    @Bean
    @Singleton
    public TicTacToeConfigurationManager ticTacToeConfigurationManager() {
        return new TicTacToeConfigurationManager(gameSpecificationRepository);
    }

    @Bean
    @Singleton()
    public GameProcessorFactory<TicTacToeState> ticTacToeProcessorFactory() {
        return new GameProcessorFactory<TicTacToeState>(new TicTacToeProcessor(), gameSecurityAspectFactory, gameBetAspectFactory, gamePriceAspectFactory,
                gameTimeAspectFactory, gameOutcomeAspectFactory);
    }

    @Bean
    @Singleton
    public GameCacheService<TicTacToeState> ticTacToeCacheService() {
        return new GameCacheService<TicTacToeState>(gameConstructionRepository, gameSessionRepository, ticTacToeProcessorFactory(), ticTacToeStateFactory());
    }

    @Bean
    @Singleton
    public GameSessionProcessor<TicTacToeState> ticTacToeSessionProcessor() {
        return new GameSessionProcessor<TicTacToeState>(Game.pic, ticTacToeTableFactory(), ticTacToeCacheService(), playerNotificationService);
    }

}
