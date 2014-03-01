package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.RoundGameConfiguration;
import com.clemble.casino.integration.game.GamePlayer;
import com.clemble.casino.integration.game.GamePlayerFactory;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.RoundGamePlayer;

public class SimpleGameScenarios implements GameScenarios {

    final private PlayerScenarios playerOperations;
    final private GamePlayerFactory playerFactory;

    public SimpleGameScenarios(PlayerScenarios playerOperations, GamePlayerFactory playerFactory) {
        this.playerOperations = checkNotNull(playerOperations);
        this.playerFactory = checkNotNull(playerFactory);
    }

    @Override
    public <State extends GameState> List<RoundGamePlayer<State>> match(Game game) {
        if (game == null)
            throw new IllegalArgumentException("Name must not be null");
        ClembleCasinoOperations A = playerOperations.createPlayer();
        // Step 0. Sanity check
        RoundGameConfiguration configuration = GameScenariosUtils.random(A.gameConstructionOperations().getConfigurations().matchConfigurations(game));
        // Step 1. Selecting configuration for the game
        return match(configuration);
    }

    @Override
    public <State extends GameState> List<RoundGamePlayer<State>> match(GameConfiguration configuration) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        // Step 1. Constructing players
        List<RoundGamePlayer<State>> constructedGames = construct(configuration);
        // Step 2. Putting player to move as first
        Collections.sort(constructedGames, new Comparator<RoundGamePlayer<State>>() {
            @Override
            public int compare(RoundGamePlayer<State> playerA, RoundGamePlayer<State> playerB) {
                return playerA.isToMove() ? -1 : playerB.isToMove() ? 1 : 0;
            }
        });
        // Step 3. Returning constructed games
        return constructedGames;
    }

    @Override
    public GamePlayer construct(GameConfiguration configuration, ClembleCasinoOperations player) {
        if (configuration instanceof MatchGameConfiguration) {
            GameConstruction construction = player.gameConstructionOperations().constructAutomatch(configuration);
            return playerFactory.construct(player, construction);
        } else if(configuration instanceof RoundGameConfiguration) {
            return match(configuration, player);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public <T extends RoundGamePlayer<?>> T match(Game game, ClembleCasinoOperations initiator) {
        return match(GameScenariosUtils.random(initiator, game), initiator);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T match(GameConfiguration configuration, ClembleCasinoOperations initiator) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.gameConstructionOperations().constructAutomatch(configuration);
        // Step 3. Returning constructed player
        return playerFactory.construct(initiator, construction);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T match(Game game, ClembleCasinoOperations initiator, String... participants) {
        return match(GameScenariosUtils.random(initiator, game), initiator, participants);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T match(GameConfiguration configuration, ClembleCasinoOperations initiator, String... participants) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.gameConstructionOperations().constructAvailability(configuration, Arrays.asList(participants));
        return playerFactory.construct(initiator, construction);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T accept(GameSessionKey sessionKey, ClembleCasinoOperations participant) {
        // Step 1. Fetching construction
        GameConstruction construction = participant.gameConstructionOperations().accept(sessionKey);
        // Step 2. Generating GameSessionPlayer
        return playerFactory.construct(participant, construction);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T match(GameSessionKey sessionKey, ClembleCasinoOperations player) {
        // Step 1. Generating GameSessionPlayer
        return playerFactory.construct(player, player.gameConstructionOperations().getConstruct(sessionKey));
    }

    @Override
    public List<MatchGamePlayer> pot() {
        ClembleCasinoOperations player = playerOperations.createPlayer();
        return pot(player.gameConstructionOperations().getConfigurations().potConfigurations().get(0));
    }

    @Override
    public List<MatchGamePlayer> pot(MatchGameConfiguration configuration) {
        return construct(configuration);
    }

    public <T extends GamePlayer> List<T> construct(GameConfiguration configuration) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        List<T> gamePlayers = new ArrayList<>();
        // Step 0. Generating players
        int numPlayers = configuration.getNumberRule().getMaxPlayers();
        // Step 1. Generating players
        List<ClembleCasinoOperations> players = new ArrayList<>();
        List<String> participants = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            ClembleCasinoOperations participant = playerOperations.createPlayer();
            players.add(participant);
            participants.add(participant.getPlayer());
        }
        // Step 2. Creating availability game request
        GameConstruction construction = players.get(0).gameConstructionOperations().constructAvailability(configuration, participants);
        gamePlayers.add(playerFactory.<T>construct(players.get(0), construction));
        GameSessionKey sessionKey = construction.getSession();
        for (int i = 1; i < numPlayers; i++) {
            GameConstruction gameConstruction = players.get(i).gameConstructionOperations().accept(sessionKey);
            T gamePlayer = playerFactory.construct(players.get(i), gameConstruction);
            gamePlayers.add(gamePlayer);
        }
        // Step 3. Waiting until all will be in sync
        for (T gamePlayer : gamePlayers)
            gamePlayer.waitForStart();
        // Step 4. Returning constructed games
        return gamePlayers;

    }

}
