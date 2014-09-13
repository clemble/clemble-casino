package com.clemble.casino.server.game.construction.listener;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.server.event.player.SystemPlayerCreatedEvent;
import com.clemble.casino.server.game.construction.PendingPlayer;
import com.clemble.casino.server.player.notification.SystemEventListener;
import com.clemble.casino.server.game.construction.repository.PendingPlayerRepository;

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
        return SystemPlayerCreatedEvent.CHANNEL + " > game:construction";
    }

}
