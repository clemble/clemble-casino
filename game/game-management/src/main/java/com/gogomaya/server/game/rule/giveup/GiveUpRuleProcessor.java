package com.gogomaya.server.game.rule.giveup;

import com.gogomaya.server.game.action.GameProcessor;
import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.impl.AbstractGameProcessor;
import com.gogomaya.server.game.action.move.GameMove;

public class GiveUpRuleProcessor<State extends GameState> extends AbstractGameProcessor<State> {

    final GiveUpRule giveUpRule;

    public GiveUpRuleProcessor(final GiveUpRule giveUpRule, final GameProcessor<State> delegate) {
        super(delegate);
        this.giveUpRule = giveUpRule;
    }

    @Override
    public void processMovement(State state, GameMove move) {
        state.getPlayerState(move.getPlayerId());
    }

}
