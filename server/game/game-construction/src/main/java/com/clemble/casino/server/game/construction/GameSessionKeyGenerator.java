package com.clemble.casino.server.game.construction;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameConfiguration;
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

    public String generate(Game game) {
        return game.name() + keyFactory.generate();
    }

    public String generate(GameConfiguration gameConfiguration) {
        return gameConfiguration.getGame() + keyFactory.generate();
    }

}
