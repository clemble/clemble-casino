package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.lifecycle.management.event.action.bet.BetAction;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;

import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class BetRuleAspectFactory implements RoundGameAspectFactory<PlayerAction<BetAction>> {

    @Override
    public GameAspect<PlayerAction<BetAction>> construct(RoundGameConfiguration configuration, RoundGameState roundState) {
        return new GameBetRuleAspect(roundState.getContext(), configuration.getBetRule());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
