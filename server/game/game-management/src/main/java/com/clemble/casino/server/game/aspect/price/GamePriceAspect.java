package com.clemble.casino.server.game.aspect.price;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.account.GamePlayerAccount;
import com.clemble.casino.game.event.client.BetAction;
import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.event.client.surrender.SurrenderAction;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GamePriceAspect<State extends GameState> extends BasicGameAspect<State> {

    @Override
    public void beforeMove(final State state, final GameAction move) {
        // Step 1. Sanity check
        if (move == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveUndefined);
        // Step 2. Checking player participate in the game
        if (!(move instanceof SurrenderAction)) {
            // Step 3. Checking that move
            if (move instanceof BetAction) {
                GamePlayerAccount gamePlayerState = state.getAccount().getPlayerAccount(move.getPlayer());
                if (((BetAction) move).getBet() > gamePlayerState.getMoneyLeft())
                    throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetOverflow);
            }
        }
    }

}
