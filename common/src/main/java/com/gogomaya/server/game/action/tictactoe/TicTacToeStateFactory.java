package com.gogomaya.server.game.action.tictactoe;

import java.util.HashSet;
import java.util.Set;

import com.gogomaya.server.game.GameSpecification;
import com.gogomaya.server.game.action.GameStateFactory;
import com.gogomaya.server.game.bet.rule.BetFixedRule;

public class TicTacToeStateFactory implements GameStateFactory {
    
    @Override
    public TicTacToeState initialize(final GameSpecification gameSpecification, final Set<Long> playerIds) {
        // Step 0. Create initial state
        if(gameSpecification == null)
            throw new IllegalArgumentException("Game specification can't be null");
        if(playerIds == null || playerIds.size() == 0)
            throw new IllegalArgumentException("Players can't be null or empty");
        // Step 1. Generating initial specification
        if(!(gameSpecification.getBetRule() instanceof BetFixedRule))
            throw new IllegalArgumentException("BetRule must be FixedBetRule");
        // Step 2. Create fixed bet rule
        BetFixedRule fixedBetRule = (BetFixedRule) gameSpecification.getBetRule();
        long price = fixedBetRule.getPrice();
        Set<TicTacToePlayerState> playerStates = new HashSet<TicTacToePlayerState>();
        for(Long playerId: playerIds) {
            playerStates.add(new TicTacToePlayerState(playerId, price));
        }
        return new TicTacToeState(playerStates);
    }

}
