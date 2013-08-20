package com.gogomaya.server.game.aspect.bet;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.aspect.BasicGameAspect;
import com.gogomaya.server.game.event.client.BetEvent;
import com.gogomaya.server.game.rule.bet.BetRule;

public class GameBetAspect<State extends GameState> extends BasicGameAspect<State> {

    final private BetRule betRule;

    public GameBetAspect(BetRule betRule) {
        this.betRule = betRule;
    }

    @Override
    public void beforeMove(GameSession<State> session, ClientEvent move) {
        if (move instanceof BetEvent) {
            if (!betRule.isValid((BetEvent) move)) {
                throw GogomayaException.fromError(GogomayaError.GamePlayBetInvalid);
            }
        }
    }

}
