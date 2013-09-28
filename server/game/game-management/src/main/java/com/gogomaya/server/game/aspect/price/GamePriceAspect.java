package com.gogomaya.server.game.aspect.price;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.account.GamePlayerAccount;
import com.gogomaya.game.event.client.BetEvent;
import com.gogomaya.game.event.client.surrender.SurrenderEvent;
import com.gogomaya.server.game.aspect.BasicGameAspect;

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
