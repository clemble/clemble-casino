package com.gogomaya.server.tictactoe.action.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.impl.AbstractGameStateFactory;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToeState;

public class TicTacToeStateFactory extends AbstractGameStateFactory<TicTacToeState> {

    public TicTacToeStateFactory(GameProcessorFactory<TicTacToeState> processorFactory) {
        super(processorFactory);
    }

    @Override
    public TicTacToeState emptyState() {
        return new TicTacToeState();
    }

    @Override
    public TicTacToeState constructState(final GameSpecification gameSpecification, final Collection<Long> playerIds) {
        // Step 0. Create initial state
        if (gameSpecification == null)
            throw new IllegalArgumentException("Game specification can't be null");
        // Step 1. Generating initial specification
        if (!(gameSpecification.getBetRule() instanceof FixedBetRule))
            throw new IllegalArgumentException("BetRule must be FixedBetRule");
        // Step 2. Create fixed bet rule
        FixedBetRule fixedBetRule = (FixedBetRule) gameSpecification.getBetRule();
        long price = fixedBetRule.getPrice();
        List<GamePlayerState> playerStates = new ArrayList<GamePlayerState>();
        for (Long playerId : playerIds) {
            playerStates.add(new GamePlayerState(playerId, price));
        }
        // Step 3. Initializing next player
        return new TicTacToeState(playerStates);
    }

}
