package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameBoard implements GameAware<GameBoard> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -3001211200759794104L;

    private String gameName;

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public GameBoard setGameName(String newGameName) {
        this.gameName = checkNotNull(gameName);
        return this;
    }
}
