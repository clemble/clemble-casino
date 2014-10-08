package com.clemble.casino.server.game.aspect.bet;

import com.clemble.casino.game.lifecycle.management.RoundGameState;
import com.clemble.casino.lifecycle.management.event.bet.PlayerBetAction;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;

import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class BetRuleAspectFactory implements RoundGameAspectFactory<PlayerBetAction> {

    @Override
    public GameAspect<PlayerBetAction> construct(RoundGameConfiguration configuration, RoundGameState roundState) {
        return new GameBetRuleAspect(roundState.getContext(), configuration.getBetRule());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

}
