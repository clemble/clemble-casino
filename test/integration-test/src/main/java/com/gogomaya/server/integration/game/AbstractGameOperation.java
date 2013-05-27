package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.game.action.GameState;
import com.gogomaya.server.game.action.GameTable;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.google.common.collect.ImmutableList;

abstract public class AbstractGameOperation<State extends GameState> implements GameOperations<State> {

    final private GamePlayerFactory<State> playerFactory;

    final private PlayerOperations playerOperations;

    protected AbstractGameOperation(final PlayerOperations playerOperations, final GamePlayerFactory<State> playerFactory) {
        this.playerFactory = checkNotNull(playerFactory);
        this.playerOperations = checkNotNull(playerOperations);
    }

    @Override
    final public GameSpecification selectSpecification() {
        GameSpecificationOptions specificationOptions = getOptions();
        if (specificationOptions instanceof SelectSpecificationOptions) {
            return ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    final public GameSpecification selectSpecification(Player player) {
        GameSpecificationOptions specificationOptions = getOptions(player);
        if (specificationOptions instanceof SelectSpecificationOptions) {
            return ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    final public GamePlayer<State> start(GameSpecification specification) {
        // Step 1. Creating player
        Player player = checkNotNull(playerOperations.createPlayer());
        // Step 2. Creating appropriate GamePlayer
        return initialize(player, specification);
    }

    @Override
    public List<GamePlayer<State>> start() {
        // Step 1. Selecting specification for the game
        return construct(selectSpecification());
    }

    @Override
    public List<GamePlayer<State>> construct(GameSpecification specification) {
        // Step 1. Creating user and trying to put them on the same table
        GamePlayer<State> playerA = start(specification);
        GamePlayer<State> playerB = start(specification);
        while (playerA.getTableId() != playerB.getTableId()) {
            playerA.clear();
            playerA = start(specification);
            // waits added to be sure everyone on the same page
            if (playerA.getTableId() != playerB.getTableId()) {
                playerB.clear();
                playerB = start(specification);
            }
        }
        playerA.syncWith(playerB);
        // Step 3. Returning generated value who ever goes first is choosen as first
        State state = playerB.getState() != null ? playerB.getState() : playerA.getState();
        if (state.getNextMove(playerA.getPlayer().getPlayerId()) == null) {
            return ImmutableList.<GamePlayer<State>> of(playerB, playerA);
        } else {
            return ImmutableList.<GamePlayer<State>> of(playerA, playerB);
        }
    }

    @Override
    final public GamePlayer<State> initialize(Player player, GameSpecification specification) {
        // Step 1. Connecting to the table
        GameTable<State> table = start(player, specification);
        // Step 2. Creating appropriate player
        return playerFactory.construct(player, table);
    }

}
