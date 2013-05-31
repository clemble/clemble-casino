package com.gogomaya.server.game.action.impl;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.move.BetMove;
import com.gogomaya.server.game.action.move.GameMove;
import com.gogomaya.server.game.action.move.GiveUpMove;

public class VerificationGameProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    public VerificationGameProcessor(final GameProcessor<State> delegate) {
        super(delegate);
    }

    @Override
    public void processMovement(State state, GameMove move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
        // Step 2. Checking player participate in the game
        final long playerId = move.getPlayerId();
        if (!state.getPlayerIterator().contains(playerId)) {
            throw GogomayaException.create(GogomayaError.GamePlayPlayerNotParticipate);
        }
        if (!(move instanceof GiveUpMove)) {
            // Step 3. Checking that move
            GameMove expectedMove = state.getNextMove(playerId);
            if (expectedMove == null)
                throw GogomayaException.create(GogomayaError.GamePlayNoMoveExpected);
            if (expectedMove.getClass() != move.getClass())
                throw GogomayaException.create(GogomayaError.GamePlayWrongMoveType);
            if (move instanceof BetMove) {
                GamePlayerState gamePlayerState = state.getPlayerState(move.getPlayerId());
                if (((BetMove) move).getBet() > gamePlayerState.getMoneyLeft())
                    throw GogomayaException.create(GogomayaError.GamePlayBetOverflow);
            }
        }
    }

}
