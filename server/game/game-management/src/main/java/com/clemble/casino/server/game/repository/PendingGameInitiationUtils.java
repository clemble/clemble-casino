package com.clemble.casino.server.game.repository;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.*;

import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.configuration.GameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.pending.PendingGameInitiation;
import com.clemble.casino.server.game.pending.PendingPlayer;

public class PendingGameInitiationUtils {

    final private PendingPlayerRepository playerRepository;
    final private PendingGameInitiationRepository initiationRepository;

    public PendingGameInitiationUtils(PendingPlayerRepository playerRepository, PendingGameInitiationRepository initiationRepository) {
        this.playerRepository = checkNotNull(playerRepository);
        this.initiationRepository = checkNotNull(initiationRepository);
    }

    public void add(final GameInitiation initiation) {
        final Collection<String> players = initiation.getParticipants();
        Set<PendingPlayer> pendingPlayers = new HashSet<>();
        for (String player : players)
            pendingPlayers.add(playerRepository.findByPropertyValue("player", player));
        // Step 1. Saving new PendingGameInitiation
        initiationRepository.save(new PendingGameInitiation(initiation, pendingPlayers));
    }

    public List<PendingGameInitiation> findPending(String player) {
        return initiationRepository.findPending(player);
    }

    public Collection<GameInitiation> getPending(String player) {
        // Step 1. Fetching data from pending player
        Collection<GameInitiation> initiations = new ArrayList<>();
        for (PendingGameInitiation initiation : playerRepository.findPending(player))
            initiations.add(toInitiation(initiation));
        // Step 2. Returning pending initiations
        return initiations;
    }

    public GameInitiation toInitiation(PendingGameInitiation initiation) {
        GameConfiguration configuration = initiation.getConfiguration();
        return new GameInitiation(initiation.getSessionKey(), configuration, PlayerAwareUtils.toPlayerList(initiation.getParticipants()));
    }
}
