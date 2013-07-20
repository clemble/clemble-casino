package com.gogomaya.server.integration.game.construction;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.integration.game.GameSessionPlayer;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;

public class GameScenarios {

    final private PlayerOperations playerOperations;

    public GameScenarios(PlayerOperations playerOperations) {
        this.playerOperations = checkNotNull(playerOperations);
    }

    public <State extends GameState> List<GameSessionPlayer<State>> constructGame(String name) {
        if (name == null)
            throw new IllegalArgumentException("Name must not be null");
        Player randomPlayer = playerOperations.createPlayer();
        // Step 0. Sanity check
        GameSpecification specification = randomPlayer.getGameConstructor(name).selectSpecification();
        // Step 1. Selecting specification for the game
        return constructGame(specification);
    }

    public <State extends GameState> List<GameSessionPlayer<State>> constructGame(GameSpecification specification) {
        if (specification == null || specification.getName() == null || specification.getName().getName() == null)
            throw new IllegalArgumentException("Specification is invalid");
        String gameName = specification.getName().getName();
        List<GameSessionPlayer<State>> constructedGames = new ArrayList<>();
        // Step 0. Generating players
        int numPlayers = specification.getNumberRule().getMaxPlayers();
        // Step 1. Generating players
        List<Player> players = new ArrayList<>();
        List<Long> participants = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Player participant = playerOperations.createPlayer();
            players.add(participant);
            participants.add(participant.getPlayerId());
        }
        // Step 2. Creating availability game request
        GameSessionPlayer<State> sessionPlayer = players.get(0).<State> getGameConstructor(gameName).constructAvailability(specification, participants);
        constructedGames.add(sessionPlayer);
        long construction = sessionPlayer.getSession();
        for (int i = 1; i < numPlayers; i++) {
            constructedGames.add(players.get(i).<State> getGameConstructor(gameName).acceptInvitation(construction));
        }
        // Step 3. Waiting until all will be in sync
        for(GameSessionPlayer<State> player: constructedGames)
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

}
