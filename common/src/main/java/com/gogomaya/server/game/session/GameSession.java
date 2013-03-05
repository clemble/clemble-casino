package com.gogomaya.server.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameRuleSpecification;

public class GameSession implements GameSessionAware<GameSession> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    private String sessionId;

    private GameSessionState sessionState;

    private GameRuleSpecification gameRuleSpecification;

    private GameState gameState;

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public GameSession sesSessionId(String sessionId) {
        this.sessionId = checkNotNull(sessionId);
        return this;
    }

    public GameSessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(GameSessionState gameSessionState) {
        this.sessionState = gameSessionState;
    }

    public GameRuleSpecification getGameRuleSpecification() {
        return gameRuleSpecification;
    }

    public void setGameRuleSpecification(GameRuleSpecification gameRuleSpecification) {
        this.gameRuleSpecification = gameRuleSpecification;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
