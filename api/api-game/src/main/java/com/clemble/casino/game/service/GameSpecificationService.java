package com.clemble.casino.game.service;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.configuration.GameSpecificationOptions;

public interface GameSpecificationService {

    public GameSpecificationOptions getSpecificationOptions(final String player, Game game);

}
