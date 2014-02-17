package com.clemble.casino.integration.game.construction;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.game.specification.PotGameConfiguration;
import com.clemble.casino.integration.game.MatchGamePlayer;
import com.clemble.casino.integration.game.MatchGamePlayerFactory;
import com.clemble.casino.integration.game.PotGamePlayer;
import com.clemble.casino.integration.game.PotGamePlayerFactory;
import com.clemble.casino.integration.game.SimpleMatchGamePlayerFactory;

public class SimpleGameScenarios implements GameScenarios, ApplicationContextAware {

    final private PlayerScenarios playerOperations;
    final private MatchGamePlayerFactory<?> DEFAULT_FACTORY = new SimpleMatchGamePlayerFactory<>();
    final private PotGamePlayerFactory potPlayerFactory = new PotGamePlayerFactory();
    final private ConcurrentHashMap<Game, MatchGamePlayerFactory<?>> gameToSessionPlayerFactory = new ConcurrentHashMap<>();

    public SimpleGameScenarios(PlayerScenarios playerOperations) {
        this.playerOperations = checkNotNull(playerOperations);
    }

    public <State extends GameState> List<MatchGamePlayer<State>> match(Game game) {
        if (game == null)
            throw new IllegalArgumentException("Name must not be null");
        ClembleCasinoOperations A = playerOperations.createPlayer();
        // Step 0. Sanity check
        MatchGameConfiguration configuration = GameScenariosUtils.random(A.gameConstructionOperations().getConfigurations().matchConfigurations(game));
        // Step 1. Selecting configuration for the game
        return match(configuration);
    }

    @SuppressWarnings("unchecked")
    public <State extends GameState> List<MatchGamePlayer<State>> match(GameConfiguration configuration) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        Game game = configuration.getConfigurationKey().getGame();
        List<MatchGamePlayer<State>> constructedGames = new ArrayList<>();
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
        MatchGamePlayer<State> sessionPlayer = (MatchGamePlayer<State>) gameToSessionPlayerFactory.get(game).construct(players.get(0), construction);
        constructedGames.add(sessionPlayer);
        GameSessionKey sessionKey = sessionPlayer.getSession();
        for (int i = 1; i < numPlayers; i++) {
            GameConstruction iPlayerConstruction = players.get(i).gameConstructionOperations().accept(sessionKey);
            MatchGamePlayer<State> iSessionPlayer = (MatchGamePlayer<State>) gameToSessionPlayerFactory.get(game).construct(players.get(i), iPlayerConstruction);
            constructedGames.add(iSessionPlayer);
        }
        // Step 3. Waiting until all will be in sync
        for (MatchGamePlayer<State> player : constructedGames)
            player.waitForStart();
        // Step 4. Putting player to move as first
        Collections.sort(constructedGames, new Comparator<MatchGamePlayer<State>>() {
            @Override
            public int compare(MatchGamePlayer<State> playerA, MatchGamePlayer<State> playerB) {
                return playerA.isToMove() ? -1 : playerB.isToMove() ? 1 : 0;
            }
        });
        // Step 5. Returning constructed games
        return constructedGames;
    }

    @Override
    public <State extends GameState> MatchGamePlayer<State> match(Game game, ClembleCasinoOperations initiator) {
        return match(GameScenariosUtils.random(initiator, game), initiator);
    }

    @Override
    public <State extends GameState> MatchGamePlayer<State> match(GameConfiguration configuration, ClembleCasinoOperations initiator) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.gameConstructionOperations().constructAutomatch(configuration);
        // Step 3. Returning constructed player
        return toSessionPlayer(initiator, construction);
    }

    @Override
    public <State extends GameState> MatchGamePlayer<State> match(Game game, ClembleCasinoOperations initiator, String... participants) {
        return match(GameScenariosUtils.random(initiator, game), initiator, participants);
    }

    @Override
    public <State extends GameState> MatchGamePlayer<State> match(GameConfiguration configuration, ClembleCasinoOperations initiator, String... participants) {
        if (configuration == null || configuration.getConfigurationKey() == null || configuration.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.gameConstructionOperations().constructAvailability(configuration, Arrays.asList(participants));
        return toSessionPlayer(initiator, construction);
    }

    @Override
    public <State extends GameState> MatchGamePlayer<State> accept(GameSessionKey sessionKey, ClembleCasinoOperations participant) {
        // Step 1. Fetching construction
        GameConstruction construction = participant.gameConstructionOperations().accept(sessionKey);
        // Step 2. Generating GameSessionPlayer
        return toSessionPlayer(participant, construction);
    }

    @Override
    public <State extends GameState> MatchGamePlayer<State> match(GameSessionKey sessionKey, ClembleCasinoOperations player) {
        // Step 1. Generating GameSessionPlayer
        return toSessionPlayer(player, player.gameConstructionOperations().getConstruct(sessionKey));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        for(MatchGamePlayerFactory<?> sessionPlayerFactory: applicationContext.getBeansOfType(MatchGamePlayerFactory.class).values())
            gameToSessionPlayerFactory.put(sessionPlayerFactory.getGame(), sessionPlayerFactory);
    }

    @SuppressWarnings("unchecked")
    private <S extends GameState> MatchGamePlayer<S> toSessionPlayer(ClembleCasinoOperations player, GameConstruction construction) {
        Game game = construction.getSession().getGame();
        if (gameToSessionPlayerFactory.get(game) != null)
            return (MatchGamePlayer<S>) gameToSessionPlayerFactory.get(game).construct(player, construction);
        // Step 3. Returning constructed player
        return (MatchGamePlayer<S>) DEFAULT_FACTORY.construct(player, construction);
    }

    @Override
    public List<PotGamePlayer> pot() {
        ClembleCasinoOperations player = playerOperations.createPlayer();
        return pot(player.gameConstructionOperations().getConfigurations().potConfigurations().get(0));
    }

    @Override
    public List<PotGamePlayer> pot(PotGameConfiguration configuration) {
        List<PotGamePlayer> constructedGames = new ArrayList<>();
        // Step 1. Creating players with participants
        int numPlayers = configuration.getNumberRule().getMinPlayers();
        List<ClembleCasinoOperations> players = new ArrayList<>();
        List<String> participants = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            ClembleCasinoOperations participant = playerOperations.createPlayer();
            players.add(participant);
            participants.add(participant.getPlayer());
        }
        // Step 2. Constructing Game
        GameConstruction potConstruction = players.get(0).gameConstructionOperations().constructAvailability(configuration, participants);
        GameSessionKey potSession = potConstruction.getSession();
        for (int i = 1; i < numPlayers; i++) {
            GameConstruction iPlayerConstruction = players.get(i).gameConstructionOperations().accept(potSession);
            PotGamePlayer iPotPlayer = potPlayerFactory.construct(players.get(i), iPlayerConstruction);
            constructedGames.add(iPotPlayer);
        }
        // Step 3. Waiting until all will be in sync
        for (PotGamePlayer player : constructedGames)
            player.waitForStart();
        // Step 5. Returning constructed games
        return constructedGames;
    }

}
