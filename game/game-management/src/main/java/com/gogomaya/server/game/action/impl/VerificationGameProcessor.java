package com.gogomaya.server.game.action.impl;

import java.util.Collection;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.GiveUpEvent;
import com.gogomaya.server.game.event.server.GameEvent;

public class VerificationGameProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    public VerificationGameProcessor(final GameProcessor<State> delegate) {
        super(delegate);
    }

    @Override
    public void beforeMove(final GameSession<State> session, final ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.create(GogomayaError.GamePlayMoveUndefined);
        State state = session.getState();
        // Step 2. Checking player participate in the game
        final long playerId = move.getPlayerId();
        if (!state.getPlayerIterator().contains(playerId)) {
            throw GogomayaException.create(GogomayaError.GamePlayPlayerNotParticipate);
        }
        if (!(move instanceof GiveUpEvent)) {
            // Step 3. Checking that move
            ClientEvent expectedMove = state.getNextMove(playerId);
            if (expectedMove == null)
                throw GogomayaException.create(GogomayaError.GamePlayNoMoveExpected);
            if (expectedMove.getClass() != move.getClass())
                throw GogomayaException.create(GogomayaError.GamePlayWrongMoveType);
            if (move instanceof BetEvent) {
                GamePlayerState gamePlayerState = state.getPlayerState(move.getPlayerId());
                if (((BetEvent) move).getBet() > gamePlayerState.getMoneyLeft())
                    throw GogomayaException.create(GogomayaError.GamePlayBetOverflow);
            }
        }
    }

    @Override
    public Collection<GameEvent<State>> afterMove(final GameSession<State> session, final Collection<GameEvent<State>> madeMoves) {
        return madeMoves;
    }

}
