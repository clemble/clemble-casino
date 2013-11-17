package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.configuration.SelectSpecificationOptions;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.game.GameSessionPlayerFactory;
import com.clemble.casino.integration.player.Player;
import com.clemble.casino.integration.player.PlayerOperations;
import com.clemble.casino.integration.util.RandomUtils;

public class SimpleGameScenarios implements GameScenarios {

    final private PlayerOperations playerOperations;
    final private GameSessionPlayerFactory sessionPlayerFactory;

    public SimpleGameScenarios(PlayerOperations playerOperations, GameSessionPlayerFactory sessionPlayerFactory) {
        this.playerOperations = checkNotNull(playerOperations);
        this.sessionPlayerFactory = checkNotNull(sessionPlayerFactory);
    }

    public <State extends GameState> List<GameSessionPlayer<State>> constructGame(Game game) {
        if (game == null)
            throw new IllegalArgumentException("Name must not be null");
        Player randomPlayer = playerOperations.createPlayer();
        // Step 0. Sanity check
        GameSpecification specification = selectSpecification(randomPlayer, game);
        // Step 1. Selecting specification for the game
        return constructGame(specification);
    }

    public <State extends GameState> List<GameSessionPlayer<State>> constructGame(GameSpecification specification) {
        if (specification == null || specification.getName() == null || specification.getName().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        Game game = specification.getName().getGame();
        List<GameSessionPlayer<State>> constructedGames = new ArrayList<>();
        // Step 0. Generating players
        int numPlayers = specification.getNumberRule().getMaxPlayers();
        // Step 1. Generating players
        List<Player> players = new ArrayList<>();
        List<String> participants = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Player participant = playerOperations.createPlayer();
            players.add(participant);
            participants.add(participant.getPlayer());
        }
        // Step 2. Creating availability game request
        GameConstruction construction = players.get(0).<State> getGameConstructor(game).constructAvailability(specification, participants);
        GameSessionPlayer<State> sessionPlayer = sessionPlayerFactory.construct(players.get(0), construction);
        constructedGames.add(sessionPlayer);
        GameSessionKey sessionKey = sessionPlayer.getSession();
        for (int i = 1; i < numPlayers; i++) {
            GameConstruction iPlayerConstruction = players.get(i).<State> getGameConstructor(game).accept(sessionKey.getSession());
            GameSessionPlayer<State> iSessionPlayer = sessionPlayerFactory.construct(players.get(i), iPlayerConstruction);
            constructedGames.add(iSessionPlayer);
        }
        // Step 3. Waiting until all will be in sync
        for (GameSessionPlayer<State> player : constructedGames)
            player.waitForStart();
        // Step 4. Putting player to move as first
        Collections.sort(constructedGames, new Comparator<GameSessionPlayer<State>>() {
            @Override
            public int compare(GameSessionPlayer<State> playerA, GameSessionPlayer<State> playerB) {
                return playerA.isToMove() ? -1 : playerB.isToMove() ? 1 : 0;
            }
        });
        // Step 5. Returning constructed games
        return constructedGames;
    }

    private GameSpecification selectSpecification(Player player, Game game) {
        GameSpecificationOptions specificationOptions = player.getGameConstructor(game).get();
        if(specificationOptions instanceof SelectSpecificationOptions) {
            return ((SelectSpecificationOptions) specificationOptions).getSpecifications().get(RandomUtils.RANDOM.nextInt(((SelectSpecificationOptions) specificationOptions).getSpecifications().size()));
        } else {
            throw new UnsupportedOperationException("This specification options not supported " + specificationOptions);
        }
    }

}
