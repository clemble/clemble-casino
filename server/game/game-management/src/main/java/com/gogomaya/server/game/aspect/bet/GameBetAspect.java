package com.gogomaya.server.game.aspect.bet;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.event.client.BetEvent;
import com.gogomaya.game.rule.bet.BetRule;
import com.gogomaya.server.game.aspect.BasicGameAspect;

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
