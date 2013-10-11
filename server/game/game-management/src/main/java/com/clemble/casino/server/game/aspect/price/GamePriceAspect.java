package com.clemble.casino.server.game.aspect.price;

import com.clemble.casino.error.GogomayaError;
import com.clemble.casino.error.GogomayaException;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.game.event.client.surrender.SurrenderEvent;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GamePriceAspect<State extends GameState> extends BasicGameAspect<State> {

    @Override
    public void beforeMove(final State state, final ClientEvent move) {
        // Step 1. Sanity check
        if (move == null)
            throw GogomayaException.fromError(GogomayaError.GamePlayMoveUndefined);
        // Step 2. Checking player participate in the game
        if (!(move instanceof SurrenderEvent)) {
            // Step 3. Checking that move
            if (move instanceof BetEvent) {
                GamePlayerAccount gamePlayerState = state.getAccount().getPlayerAccount(move.getPlayer());
                if (((BetEvent) move).getBet() > gamePlayerState.getMoneyLeft())
                    throw GogomayaException.fromError(GogomayaError.GamePlayBetOverflow);
            }
        }
    }

}