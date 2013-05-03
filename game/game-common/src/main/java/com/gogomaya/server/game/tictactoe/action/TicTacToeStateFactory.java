package com.gogomaya.server.game.tictactoe.action;

import java.util.HashSet;
import java.util.Set;

import com.gogomaya.server.game.action.GamePlayerState;
import com.gogomaya.server.game.action.impl.AbstractGameStateFactory;
import com.gogomaya.server.game.rule.bet.FixedBetRule;
import com.gogomaya.server.game.specification.GameSpecification;

public class TicTacToeStateFactory extends AbstractGameStateFactory<TicTacToeState> {

    @Override
    public TicTacToeState create(final GameSpecification gameSpecification, final Set<Long> playerIds) {
        // Step 0. Create initial state
        if (gameSpecification == null)
            throw new IllegalArgumentException("Game specification can't be null");
        if (playerIds == null || playerIds.size() == 0)
            throw new IllegalArgumentException("Players can't be null or empty");
        // Step 1. Generating initial specification
        if (!(gameSpecification.getBetRule() instanceof FixedBetRule))
            throw new IllegalArgumentException("BetRule must be FixedBetRule");
        // Step 2. Create fixed bet rule
        FixedBetRule fixedBetRule = (FixedBetRule) gameSpecification.getBetRule();
        long price = fixedBetRule.getPrice();
        Set<GamePlayerState> playerStates = new HashSet<GamePlayerState>();
        for (Long playerId : playerIds) {
            playerStates.add(new GamePlayerState(playerId, price));
        }
        // Step 3. Initializing next player
        return  new TicTacToeState(playerStates);
    }

}
