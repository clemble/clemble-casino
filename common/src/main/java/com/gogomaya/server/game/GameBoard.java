package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

public class GameBoard implements GameAware<GameBoard> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -3001211200759794104L;

    @JsonIgnore
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
