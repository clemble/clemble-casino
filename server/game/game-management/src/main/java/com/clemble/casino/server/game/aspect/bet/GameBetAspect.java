package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.error.GogomayaError;
import com.clemble.casino.error.GogomayaException;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.BetEvent;
import com.clemble.casino.game.rule.bet.BetRule;
import com.clemble.casino.server.game.aspect.BasicGameAspect;

public class GameBetAspect<State extends GameState> extends BasicGameAspect<State> {

    final private BetRule betRule;

    public GameBetAspect(BetRule betRule) {
        this.betRule = betRule;
    }

    @Override
    public void beforeMove(State state, ClientEvent move) {
        if (move instanceof BetEvent) {
            if (!betRule.isValid((BetEvent) move)) {
                throw GogomayaException.fromError(GogomayaError.GamePlayBetInvalid);
            }
        }
    }

}
