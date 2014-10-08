package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.*;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.game.lifecycle.configuration.GameConfigurationUtils;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
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
    public <State extends GameState> List<RoundGamePlayer<State>> round(Game game) {
        if (game == null)
            throw new IllegalArgumentException("Name must not be null");
        ClembleCasinoOperations A = playerOperations.createPlayer();
        // Step 0. Sanity check
        List<RoundGameConfiguration> roundGameConfigurations = GameConfigurationUtils.matchConfigurations(game, A.gameConstructionOperations().getConfigurations());
        RoundGameConfiguration configuration = GameScenariosUtils.random(roundGameConfigurations);
        // Step 1. Selecting configuration for the game
        return round(configuration);
    }

    @Override
    public <State extends GameState> List<RoundGamePlayer<State>> round(GameConfiguration configuration) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getGame() == null)
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
            return round(configuration, player);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public <T extends RoundGamePlayer<?>> T round(Game game, ClembleCasinoOperations initiator) {
        return round(GameScenariosUtils.random(initiator, game), initiator);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T round(GameConfiguration configuration, ClembleCasinoOperations initiator) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.gameConstructionOperations().constructAutomatch(configuration);
        // Step 3. Returning constructed player
        return playerFactory.construct(initiator, construction);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T round(Game game, ClembleCasinoOperations initiator, String... participants) {
        return round(GameScenariosUtils.random(initiator, game), initiator, participants);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T round(GameConfiguration configuration, ClembleCasinoOperations initiator, String... participants) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.gameConstructionOperations().constructAvailability(configuration, Arrays.asList(participants));
        return playerFactory.construct(initiator, construction);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T accept(String sessionKey, ClembleCasinoOperations participant) {
        // Step 1. Fetching construction
        GameConstruction construction = participant.gameConstructionOperations().accept(sessionKey);
        // Step 2. Generating GameSessionPlayer
        return playerFactory.construct(participant, construction);
    }

    @Override
    public <T extends RoundGamePlayer<?>> T round(String sessionKey, ClembleCasinoOperations player) {
        // Step 1. Generating GameSessionPlayer
        return playerFactory.construct(player, player.gameConstructionOperations().getConstruct(sessionKey));
    }

    @Override
    public List<MatchGamePlayer> match() {
        ClembleCasinoOperations player = playerOperations.createPlayer();
        List<MatchGameConfiguration> matchGameConfigurations = GameConfigurationUtils.potConfigurations(player.gameConstructionOperations().getConfigurations());
        return match(matchGameConfigurations.get(0));
    }

    @Override
    public List<MatchGamePlayer> match(MatchGameConfiguration configuration) {
        return construct(configuration);
    }

    public <T extends GamePlayer> List<T> construct(GameConfiguration configuration) {
        if (configuration == null)
            throw new IllegalArgumentException("Specification is invalid: Can't create with null CONFIGURATION");
        if (configuration.getConfigurationKey() == null || configuration.getGame() == null)
            throw new IllegalArgumentException("Specification is invalid: Can't create with null KEY");
        if (configuration.getGame() == null)
            throw new IllegalArgumentException("Specification is invalid: Can't create with null GAME");
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
        String sessionKey = construction.getSessionKey();
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
