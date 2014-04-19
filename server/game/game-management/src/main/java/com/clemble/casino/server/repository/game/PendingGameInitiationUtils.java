package com.clemble.casino.server.repository.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.PendingGameInitiation;
import com.clemble.casino.server.game.PendingPlayer;

public class PendingGameInitiationUtils {

    final private PendingPlayerRepository playerRepository;
    final private PendingGameInitiationRepository initiationRepository;

    final private ServerGameConfigurationRepository configurationRepository;

    public PendingGameInitiationUtils(PendingPlayerRepository playerRepository, PendingGameInitiationRepository initiationRepository,
            ServerGameConfigurationRepository configurationRepository) {
        this.playerRepository = checkNotNull(playerRepository);
        this.initiationRepository = checkNotNull(initiationRepository);
        this.configurationRepository = configurationRepository;
    }

    public void add(final GameInitiation initiation) {
        final Collection<String> players = initiation.getParticipants();
        Collection<PendingPlayer> pendingPlayers = new ArrayList<>();
        for (String player : players)
            pendingPlayers.add(new PendingPlayer(player));
        // Step 1. Saving new PendingGameInitiation
        initiationRepository.save(new PendingGameInitiation(initiation));
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
        GameConfiguration configuration = configurationRepository.findOne(initiation.getConfigurationKey()).getConfiguration();
        return new GameInitiation(initiation.getSession(), configuration, PlayerAwareUtils.toPlayerList(initiation.getParticipants()));
    }
}
