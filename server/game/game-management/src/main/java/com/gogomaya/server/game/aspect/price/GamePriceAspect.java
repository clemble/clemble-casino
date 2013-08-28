package com.gogomaya.server.game.aspect.price;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.account.GamePlayerAccount;
import com.gogomaya.server.game.aspect.BasicGameAspect;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.event.client.surrender.SurrenderEvent;

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
                GamePlayerAccount gamePlayerState = state.getAccount().getPlayerAccount(move.getPlayerId());
                if (((BetEvent) move).getBet() > gamePlayerState.getMoneyLeft())
                    throw GogomayaException.fromError(GogomayaError.GamePlayBetOverflow);
            }
        }
    }

}
