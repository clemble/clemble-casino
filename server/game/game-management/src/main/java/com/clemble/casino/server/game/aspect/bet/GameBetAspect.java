package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.BetAction;
import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GameBetAspect<State extends GameState> extends BasicGameAspect<State> {

    final private BetRule betRule;

    public GameBetAspect(BetRule betRule) {
        this.betRule = betRule;
    }

    @Override
    public void beforeMove(State state, GameAction move) {
        if (move instanceof BetAction) {
            if (!betRule.isValid((BetAction) move)) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayBetInvalid);
            }
        }
    }

}
