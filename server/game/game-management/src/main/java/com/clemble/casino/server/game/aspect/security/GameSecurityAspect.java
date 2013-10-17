package com.clemble.casino.server.game.aspect.security;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GameSecurityAspect<State extends GameState> extends BasicGameAspect<State> {

    @Override
    public void beforeMove(final State state, final ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveUndefined);
        // Step 2. Checking player participate in the game
        final String player = move.getPlayer();
        if (!state.getPlayerIterator().contains(player)) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayPlayerNotParticipate);
        }
    }

}
