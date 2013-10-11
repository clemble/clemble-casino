package com.clemble.casino.integration.game.construction;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;

import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameAware;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.integration.game.GameSessionPlayer;
import com.clemble.casino.integration.player.Player;

public class PlayerGameConstructionOperations<State extends GameState> implements GameAware {

    /**
     * Generated 03/07/13
     */
    private static final long serialVersionUID = 1645707988649743797L;

    final private GameConstructionOperations<State> gameOperations;
    final private Player player;

    public PlayerGameConstructionOperations(GameConstructionOperations<State> gameOperations, Player player) {
        this.gameOperations = checkNotNull(gameOperations);
        this.player = player;
    }

    @Override
    public Game getGame() {
        return gameOperations.getGame();
    }

    public GameSpecification selectSpecification() {
        return gameOperations.selectSpecification(player);
    }

    public GameSessionPlayer<State> constructAvailability(Player... participants) {
        Collection<String> participantIds = new ArrayList<>();
        for (Player player : participants)
            participantIds.add(player.getPlayer());
        return constructAvailability(participantIds);
    }

    public GameSessionPlayer<State> constructAvailability(Collection<String> participants) {
        return gameOperations.constructAvailability(player, selectSpecification(), participants);
    }

    public GameSessionPlayer<State> constructAvailability(GameSpecification specification, Collection<String> participants) {
        return gameOperations.constructAvailability(player, specification, participants);
    }

    public GameSessionPlayer<State> construct(GameRequest request) {
        return gameOperations.construct(player, request);
    }

    public GameSessionPlayer<State> acceptInvitation(GameSessionKey construction) {
        return gameOperations.acceptInvitation(player, construction);
    }

    public void declineInvitation(GameSessionKey construction) {
        gameOperations.declineInvitation(player, construction);
    }

}
