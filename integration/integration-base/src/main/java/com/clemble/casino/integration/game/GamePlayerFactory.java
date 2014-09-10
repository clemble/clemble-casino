package com.clemble.casino.integration.game;

import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.game.configuration.MatchGameConfiguration;
import com.clemble.casino.game.configuration.RoundGameConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.game.configuration.GameConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class GamePlayerFactory implements ApplicationListener<ContextRefreshedEvent> {

    final private RoundGamePlayerFactory<?> defaultMatchPlayerFactory = new SimpleRoundGamePlayerFactory<>();
    final private Map<Game, RoundGamePlayerFactory<?>> gameToPlayerFactory = new HashMap<>();

    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, String sessionKey) {
        GameConstruction construction = player.gameConstructionOperations().getConstruct(sessionKey);
        return construct(player, construction.getSessionKey(), construction.getConfiguration());
    }

    @SuppressWarnings("unchecked")
    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, String sessionKey, GameConfiguration configuration) {
        if (configuration instanceof RoundGameConfiguration) {
            Game game = configuration.getGame();
            if (gameToPlayerFactory.get(game) != null)
                return (P) gameToPlayerFactory.get(game).construct(player, sessionKey, configuration);
            return (P) defaultMatchPlayerFactory.construct(player, sessionKey, configuration);
        } else if (configuration instanceof MatchGameConfiguration) {
            return (P) new SimpleMatchGamePlayer(player, sessionKey, configuration, this);
        } else {
            throw new IllegalArgumentException();
        } 
    }

    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, GameConstruction construction) {
        return construct(player, construction.getSessionKey(), construction.getConfiguration());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        for (RoundGamePlayerFactory<?> gamePlayerFactory : event.getApplicationContext().getBeansOfType(RoundGamePlayerFactory.class).values())
            gameToPlayerFactory.put(gamePlayerFactory.getGame(), gamePlayerFactory);
    }

}
