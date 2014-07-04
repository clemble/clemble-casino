package com.clemble.casino.server.game.construction.availability;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.server.event.SystemPlayerCreatedEvent;
import com.clemble.casino.server.game.pending.PendingPlayer;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.repository.game.PendingPlayerRepository;

public class PendingPlayerCreationEventListener implements SystemEventListener<SystemPlayerCreatedEvent> {

    final private PendingPlayerRepository playerRepository;

    public PendingPlayerCreationEventListener(PendingPlayerRepository playerRepository) {
        this.playerRepository = checkNotNull(playerRepository);
    }

    @Override
    public void onEvent(SystemPlayerCreatedEvent event) {
        // TODO add fault tolerance processing
        playerRepository.save(new PendingPlayer(event.getPlayer()));
    }

    @Override
    public String getChannel() {
        return SystemPlayerCreatedEvent.CHANNEL;
    }

    @Override
    public String getQueueName() {
        return "game.pending.player.registration";
    }

}
