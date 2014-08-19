package com.clemble.casino.server.game;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.id.KeyFactory;

/**
 * Created by mavarazy on 8/19/14.
 */
public class GameSessionKeyGenerator implements KeyGenerator {

    final private KeyFactory keyFactory;

    public GameSessionKeyGenerator(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    public GameSessionKey generate(Game game) {
        return new GameSessionKey(game, keyFactory.generate());
    }

    public GameSessionKey generate(GameConfiguration gameConfiguration) {
        return new GameSessionKey(gameConfiguration.getConfigurationKey().getGame(), keyFactory.generate());
    }

}
