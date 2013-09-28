package com.gogomaya.game.service;

import com.gogomaya.game.Game;
import com.gogomaya.game.configuration.GameSpecificationOptions;

public interface GameSpecificationService {

    public GameSpecificationOptions getSpecificationOptions(final String player, Game game);

}
