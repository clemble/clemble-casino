package com.gogomaya.server.game;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameSpecification implements GameAware<GameSpecification> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -243377038921039858L;

    private String gameName;

    private int minPlayers;

    private int maxPlayers;

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public GameSpecification setGameName(String newGameName) {
        gameName = checkNotNull(newGameName);
        return this;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public GameSpecification setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
        return this;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public GameSpecification setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        return this;
    }

}
