package com.gogomaya.server.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.game.GameAware;
import com.gogomaya.server.game.GameBoard;
import com.gogomaya.server.game.GameBoardAware;

public class GameSession implements GameSessionAware<GameSession>, GameAware<GameSession>, GameBoardAware<GameSession>,
        GameSessionCurrentStateAware<GameSession> {

    /**
     * Generated 16/02/13
     */
    private static final long serialVersionUID = -6572596573895530995L;

    private String sessionId;

    private String gameName;

    private long currentPlayerId;

    private long currentStep;

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
    public String getGameName() {
        return gameName;
    }

    @Override
    public GameSession setGameName(String newGameName) {
        this.gameName = checkNotNull(gameName);
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

    @Override
    public long getCurrentPlayerId() {
        return currentPlayerId;
    }

    @Override
    public GameSession setCurrentPlayerId(long activePlayerId) {
        this.currentPlayerId = activePlayerId;
        return this;
    }

    @Override
    public long getCurrentStep() {
        return currentStep;
    }

    @Override
    public GameSession setCurrentStep(long activeStep) {
        this.currentStep = activeStep;
        return this;
    }

}
