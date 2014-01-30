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
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.game.GameSessionPlayerFactory;

public class SimpleGameScenarios implements GameScenarios, ApplicationContextAware {

    final private PlayerScenarios playerOperations;
    final private ConcurrentHashMap<Game, GameSessionPlayerFactory<?>> gameToSessionPlayerFactory = new ConcurrentHashMap<>();

    public SimpleGameScenarios(PlayerScenarios playerOperations) {
        this.playerOperations = checkNotNull(playerOperations);
    }

    public <State extends GameState> List<GameSessionPlayer<State>> construct(Game game) {
        if (game == null)
            throw new IllegalArgumentException("Name must not be null");
        ClembleCasinoOperations randomPlayer = playerOperations.createPlayer();
        // Step 0. Sanity check
        MatchGameConfiguration specification = GameScenariosUtils.random(randomPlayer.gameConstructionOperations(game).getConfigurations().matchConfigurations());
        // Step 1. Selecting specification for the game
        return construct(specification);
    }

    @SuppressWarnings("unchecked")
    public <State extends GameState> List<GameSessionPlayer<State>> construct(MatchGameConfiguration specification) {
        if (specification == null || specification.getConfigurationKey() == null || specification.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        Game game = specification.getConfigurationKey().getGame();
        List<GameSessionPlayer<State>> constructedGames = new ArrayList<>();
        // Step 0. Generating players
        int numPlayers = specification.getNumberRule().getMaxPlayers();
        // Step 1. Generating players
        List<ClembleCasinoOperations> players = new ArrayList<>();
        List<String> participants = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            ClembleCasinoOperations participant = playerOperations.createPlayer();
            players.add(participant);
            participants.add(participant.getPlayer());
        }
        // Step 2. Creating availability game request
        GameConstruction construction = players.get(0).<State> gameConstructionOperations(game).constructAvailability(specification, participants);
        GameSessionPlayer<State> sessionPlayer = (GameSessionPlayer<State>) gameToSessionPlayerFactory.get(game).construct(players.get(0), construction);
        constructedGames.add(sessionPlayer);
        GameSessionKey sessionKey = sessionPlayer.getSession();
        for (int i = 1; i < numPlayers; i++) {
            GameConstruction iPlayerConstruction = players.get(i).<State> gameConstructionOperations(game).accept(sessionKey.getSession());
            GameSessionPlayer<State> iSessionPlayer = (GameSessionPlayer<State>) gameToSessionPlayerFactory.get(game).construct(players.get(i), iPlayerConstruction);
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

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(Game game, ClembleCasinoOperations initiator) {
        return construct(GameScenariosUtils.random(initiator, game), initiator);
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(MatchGameConfiguration specification, ClembleCasinoOperations initiator) {
        if (specification == null || specification.getConfigurationKey() == null || specification.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        Game game = specification.getConfigurationKey().getGame();
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.<State> gameConstructionOperations(game).constructAutomatch(specification);
        GameSessionPlayer<State> sessionPlayer = (GameSessionPlayer<State>) gameToSessionPlayerFactory.get(game).construct(initiator, construction);
        // Step 3. Returning constructed player
        return sessionPlayer;
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(Game game, ClembleCasinoOperations initiator, String... participants) {
        return construct(GameScenariosUtils.random(initiator, game), initiator, participants);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <State extends GameState> GameSessionPlayer<State> construct(MatchGameConfiguration specification, ClembleCasinoOperations initiator, String... participants) {
        if (specification == null || specification.getConfigurationKey() == null || specification.getConfigurationKey().getGame() == null)
            throw new IllegalArgumentException("Specification is invalid");
        Game game = specification.getConfigurationKey().getGame();
        // Step 2. Creating availability game request
        GameConstruction construction = initiator.<State> gameConstructionOperations(game).constructAvailability(specification, Arrays.asList(participants));
        
        GameSessionPlayer<State> sessionPlayer = (GameSessionPlayer<State>) gameToSessionPlayerFactory.get(game).construct(initiator, construction);
        // Step 3. Returning constructed player
        return sessionPlayer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <State extends GameState> GameSessionPlayer<State> accept(GameSessionKey sessionKey, ClembleCasinoOperations participant) {
        // Step 1. Fetching construction
        GameConstruction construction = participant.gameConstructionOperations(sessionKey.getGame()).accept(sessionKey.getSession());
        // Step 2. Generating GameSessionPlayer
        return (GameSessionPlayer<State>) gameToSessionPlayerFactory.get(sessionKey.getGame()).construct(participant, construction);
    }

    @Override
    public <State extends GameState> GameSessionPlayer<State> construct(GameSessionKey sessionKey, ClembleCasinoOperations player) {
        // Step 1. Generating GameSessionPlayer
        return (GameSessionPlayer<State>) gameToSessionPlayerFactory.get(sessionKey.getGame()).construct(player, player.gameConstructionOperations(sessionKey.getGame()).getConstruct(sessionKey.getSession()));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        for(GameSessionPlayerFactory<?> sessionPlayerFactory: applicationContext.getBeansOfType(GameSessionPlayerFactory.class).values())
            gameToSessionPlayerFactory.put(sessionPlayerFactory.getGame(), sessionPlayerFactory);
    }

}
