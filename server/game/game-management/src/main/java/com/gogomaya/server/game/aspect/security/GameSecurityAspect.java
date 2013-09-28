package com.gogomaya.server.game.aspect.security;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.server.game.aspect.BasicGameAspect;

public class GameSecurityAspect<State extends GameState> extends BasicGameAspect<State> {

    @Override
    public void beforeMove(final State state, final ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.fromError(GogomayaError.GamePlayMoveUndefined);
        // Step 2. Checking player participate in the game
        final String player = move.getPlayer();
        if (!state.getPlayerIterator().contains(player)) {
            throw GogomayaException.fromError(GogomayaError.GamePlayPlayerNotParticipate);
        }
    }

}
