package com.clemble.casino.server.game.construction.availability;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.specification.GameConfiguration;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.player.Presence;
import com.clemble.casino.server.event.SystemPlayerPresenceChangedEvent;
import com.clemble.casino.server.game.pending.PendingGameInitiation;
import com.clemble.casino.server.game.pending.PendingPlayer;
import com.clemble.casino.server.game.construct.ServerGameInitiationService;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.game.PendingGameInitiationRepository;
import com.clemble.casino.server.repository.game.PendingPlayerRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class PendingGameInitiationEventListener implements SystemEventListener<SystemPlayerPresenceChangedEvent> {

    final private ServerPlayerPresenceService presenceService;
    final private ServerGameInitiationService initiationService;
    final private ServerGameConfigurationRepository configurationRepository;

    final private PendingPlayerRepository playerRepository;
    final private PendingGameInitiationRepository initiationRepository;

    public PendingGameInitiationEventListener(PendingPlayerRepository playerRepository,
            PendingGameInitiationRepository initiationRepository,
            ServerPlayerPresenceService presenceService,
            ServerGameInitiationService initiationService,
            ServerGameConfigurationRepository configurationRepository) {
        this.initiationService = checkNotNull(initiationService);
        this.presenceService = checkNotNull(presenceService);
        this.initiationRepository = checkNotNull(initiationRepository);
        this.playerRepository = checkNotNull(playerRepository);
        this.configurationRepository = checkNotNull(configurationRepository);
    }

    @Override
    public void onEvent(SystemPlayerPresenceChangedEvent event) {
        // Step 1. Checking only if presence is online
        if (event.getPresence() == Presence.online) {
            // Step 2. Fetching pending initiations
            for (PendingGameInitiation initiation : playerRepository.findPending(event.getPlayer())) {
                // Step 3. Checking we can start game
                List<String> players = PlayerAwareUtils.toPlayerList(initiation.getParticipants());
                if (presenceService.areAvailable(players)) {
                    initiationService.start(toInitiation(initiation));
                    initiationRepository.delete(initiation.getId());
                    break;
                }
            }
        }
    }

    @Override
    public String getChannel() {
        return SystemPlayerPresenceChangedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return SystemPlayerPresenceChangedEvent.CHANNEL + " > game:pending";
    }

    public void add(final GameInitiation initiation) {
        final Collection<String> players = initiation.getParticipants();
        if (!presenceService.areAvailable(players)) {
            Collection<PendingPlayer> pendingPlayers = new ArrayList<>();
            for (String player : players)
                pendingPlayers.add(new PendingPlayer(player));
            // Step 1. Saving new PendingGameInitiation
            initiationRepository.save(new PendingGameInitiation(initiation));
        } else {
            initiationService.start(initiation);
        }
    }

    // TODO add filtering by game
    public Collection<GameInitiation> getPending(String player) {
        // Step 1. Fetching data from pending player
        Collection<GameInitiation> initiations = new ArrayList<>();
        for(PendingGameInitiation initiation: playerRepository.findPending(player))
            initiations.add(toInitiation(initiation));
        // Step 2. Returning pending initiations
        return initiations;
    }

    private GameInitiation toInitiation(PendingGameInitiation initiation){
        GameConfiguration configuration = configurationRepository.findOne(initiation.getConfigurationKey()).getConfiguration();
        return new GameInitiation(initiation.getSessionKey(), configuration, PlayerAwareUtils.toPlayerList(initiation.getParticipants()));
    }

}
