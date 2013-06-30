package com.gogomaya.server.integration.game;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.configuration.GameSpecificationOptions;
import com.gogomaya.server.game.configuration.SelectSpecificationOptions;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.google.common.collect.ImmutableList;

abstract public class AbstractGameOperation<State extends GameState> implements GameOperations<State> {

    final private PlayerOperations playerOperations;
    final private GamePlayerFactory<State> playerFactory;

    protected AbstractGameOperation(final PlayerOperations playerOperations, final GamePlayerFactory<State> playerFactory) {
        this.playerOperations = checkNotNull(playerOperations);
        this.playerFactory = checkNotNull(playerFactory);
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
    final public GamePlayer<State> construct(GameSpecification specification) {
        // Step 1. Creating player
        Player player = checkNotNull(playerOperations.createPlayer());
        // Step 2. Creating appropriate GamePlayer
        return construct(player, specification);
    }

    @Override
    public List<GamePlayer<State>> constructGame() {
        // Step 1. Selecting specification for the game
        return constructGame(selectSpecification());
    }

    @Override
    public List<GamePlayer<State>> constructGame(GameSpecification specification) {
        // Step 1. Creating user and trying to put them on the same table
        GamePlayer<State> playerA = construct(specification);
        GamePlayer<State> playerB = construct(specification);
        playerA.waitForStart();
        playerB.waitForStart();
        while (playerA.getTableId() != playerB.getTableId()) {
            playerA.clear();
            playerA = construct(specification);
            // waits added to be sure everyone on the same page
            if (playerA.getTableId() != playerB.getTableId()) {
                playerB.clear();
                playerB = construct(specification);
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
    final public GamePlayer<State> construct(Player player) {
        return construct(player, selectSpecification());
    }

    @Override
    public GamePlayer<State> construct(Player player, GameSpecification specification) {
        return playerFactory.construct(player, request(player, specification));
    }

    abstract protected GameConstruction request(Player player, GameSpecification specification);

}
