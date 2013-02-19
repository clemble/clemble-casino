package com.gogomaya.server.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameBoard;
import com.gogomaya.server.game.GameBoardAware;

public class GameSession implements GameSessionAware<GameSession>, GameBoardAware<GameSession> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    private String sessionId;

    private long sequence;

    private GameSessionState gameSessionState;

    private GameBoard gameBoard;

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
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public GameSession setGameBoard(GameBoard currentBoard) {
        this.gameBoard = currentBoard;
        return this;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public GameSessionState getGameSessionState() {
        return gameSessionState;
    }

    public void setGameSessionState(GameSessionState gameSessionState) {
        this.gameSessionState = gameSessionState;
    }

}
