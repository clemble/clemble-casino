package com.gogomaya.server.tictactoe.action.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.gogomaya.server.game.GamePlayerState;
import com.gogomaya.server.game.action.GameProcessorFactory;
import com.gogomaya.server.game.action.impl.AbstractGameStateFactory;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.repository.game.GameConstructionRepository;

public class TicTacToeStateFactory extends AbstractGameStateFactory<TicTacToeState> {

    public TicTacToeStateFactory(GameConstructionRepository constructionRepository, GameProcessorFactory<TicTacToeState> processorFactory) {
        super(constructionRepository, processorFactory);
    }

    @Override
    public TicTacToeState constructState(final GameSpecification gameSpecification, final Collection<Long> playerIds) {
        // Step 0. Create initial state
        if (gameSpecification == null)
            throw new IllegalArgumentException("Game specification can't be null");
        if (gameSpecification.getPrice().getAmount() <= 0)
            throw new IllegalArgumentException("Price must be fixed");
        // Step 2. Create fixed bet rule
        long price = gameSpecification.getPrice().getAmount();
        List<GamePlayerState> playerStates = new ArrayList<GamePlayerState>();
        for (Long playerId : playerIds) {
            playerStates.add(new GamePlayerState(playerId, price));
        }
        // Step 3. Initializing next player
        return new TicTacToeState(playerStates);
    }

}
