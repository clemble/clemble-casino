package com.clemble.casino.integration.game;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.GameConfigurationKey;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;

public class GamePlayerFactory implements ApplicationContextAware {

    final private MatchGamePlayerFactory<?> defaultMatchPlayerFactory = new SimpleMatchGamePlayerFactory<>();
    final private Map<Game, MatchGamePlayerFactory<?>> gameToPlayerFactory = new HashMap<>();

    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, GameSessionKey sessionKey) {
        GameConstruction construction = player.gameConstructionOperations().getConstruct(sessionKey);
        return construct(player, construction.getSession(), construction.getRequest().getConfiguration().getConfigurationKey());
    }

    @SuppressWarnings("unchecked")
    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, GameSessionKey sessionKey, GameConfigurationKey configurationKey) {
        GameConfiguration configuration = player.gameConstructionOperations().getConfigurations().getConfiguration(configurationKey);
        if (configuration instanceof MatchGameConfiguration) {
            Game game = configuration.getConfigurationKey().getGame();
            if (gameToPlayerFactory.get(game) != null)
                return (P) gameToPlayerFactory.get(game).construct(player, sessionKey, configurationKey);
            return (P) defaultMatchPlayerFactory.construct(player, sessionKey, configurationKey);
        } else if (configuration instanceof PotGameConfiguration) {
            return (P) new SimplePotGamePlayer(player, sessionKey, configuration.getConfigurationKey(), this);
        } else {
            throw new IllegalArgumentException();
        } 
    }

    public <P extends GamePlayer> P construct(ClembleCasinoOperations player, GameConstruction construction) {
        return construct(player, construction.getSession(), construction.getRequest().getConfiguration().getConfigurationKey());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        for (MatchGamePlayerFactory<?> gamePlayerFactory : applicationContext.getBeansOfType(MatchGamePlayerFactory.class).values())
            gameToPlayerFactory.put(gamePlayerFactory.getGame(), gamePlayerFactory);
    }

}
