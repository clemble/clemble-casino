package com.gogomaya.server.game.aspect.security;

import java.util.Collection;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class GameSecurityAspect<State extends GameState> implements GameAspect<State> {

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
    }

    @Override
    public Collection<GameServerEvent<State>> afterMove(final GameSession<State> session, final Collection<GameServerEvent<State>> madeMoves) {
        return madeMoves;
    }

    @Override
    public Collection<GameServerEvent<State>> afterGame(GameSession<State> session, Collection<GameServerEvent<State>> events) {
        return events;
    }

}
