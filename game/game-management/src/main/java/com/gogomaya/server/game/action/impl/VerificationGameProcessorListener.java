package com.gogomaya.server.game.action.impl;

import java.util.Collection;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameProcessorListener;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.SurrenderEvent;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class VerificationGameProcessorListener<State extends GameState> implements GameProcessorListener<State> {

    @Override
    public void beforeMove(final GameSession<State> session, final ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.fromError(GogomayaError.GamePlayMoveUndefined);
        State state = session.getState();
        // Step 2. Checking player participate in the game
        final long playerId = move.getPlayerId();
        if (!state.getPlayerIterator().contains(playerId)) {
            throw GogomayaException.fromError(GogomayaError.GamePlayPlayerNotParticipate);
        }
        if (!(move instanceof SurrenderEvent)) {
            // Step 3. Checking that move
            if (move instanceof BetEvent) {
                GamePlayerState gamePlayerState = state.getPlayerState(move.getPlayerId());
                if (((BetEvent) move).getBet() > gamePlayerState.getMoneyLeft())
                    throw GogomayaException.fromError(GogomayaError.GamePlayBetOverflow);
            }
        }
    }

    @Override
    public Collection<GameServerEvent<State>> afterMove(final GameSession<State> session, final Collection<GameServerEvent<State>> madeMoves) {
        return madeMoves;
    }

}
