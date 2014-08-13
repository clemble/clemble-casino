package com.clemble.casino.integration.game;

import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationKey;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class GamePlayerFactory implements ApplicationListener<ContextRefreshedEvent> {

    final private RoundGamePlayerFactory<?> defaultMatchPlayerFactory = new SimpleRoundGamePlayerFactory<>();
    final private Map<Game, RoundGamePlayerFactory<?>> gameToPlayerFactory = new HashMap<>();

    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, GameSessionKey sessionKey) {
        GameConstruction construction = player.gameConstructionOperations().getConstruct(sessionKey);
        return construct(player, construction.getSessionKey(), construction.getRequest().getConfiguration().getConfigurationKey());
    }

    @SuppressWarnings("unchecked")
    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, GameSessionKey sessionKey, GameConfigurationKey configurationKey) {
        GameConfiguration configuration = player.gameConstructionOperations().getConfigurations().getConfiguration(configurationKey);
        if (configuration instanceof RoundGameConfiguration) {
            Game game = configuration.getConfigurationKey().getGame();
            if (gameToPlayerFactory.get(game) != null)
                return (P) gameToPlayerFactory.get(game).construct(player, sessionKey, configurationKey);
            return (P) defaultMatchPlayerFactory.construct(player, sessionKey, configurationKey);
        } else if (configuration instanceof MatchGameConfiguration) {
            return (P) new SimpleMatchGamePlayer(player, sessionKey, configuration.getConfigurationKey(), this);
        } else {
            throw new IllegalArgumentException();
        } 
    }

    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, GameConstruction construction) {
        return construct(player, construction.getSessionKey(), construction.getRequest().getConfiguration().getConfigurationKey());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (RoundGamePlayerFactory<?> gamePlayerFactory : event.getApplicationContext().getBeansOfType(RoundGamePlayerFactory.class).values())
            gameToPlayerFactory.put(gamePlayerFactory.getGame(), gamePlayerFactory);
    }

}
