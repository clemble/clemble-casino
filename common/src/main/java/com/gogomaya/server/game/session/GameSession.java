package com.gogomaya.server.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameAware;

public class GameSession implements GameSessionAware<GameSession>, GameAware<GameSession> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    private String sessionId;

    private String gameName;

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public GameSession sesSessionId(String sessionId) {
        this.sessionId = checkNotNull(sessionId);
        return this;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public GameSession setGameName(String newGameName) {
        this.gameName = checkNotNull(gameName);
        return this;
    }

}
