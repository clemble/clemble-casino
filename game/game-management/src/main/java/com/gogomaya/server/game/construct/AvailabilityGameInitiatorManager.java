package com.gogomaya.server.game.construct;

import java.util.Collection;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import com.gogomaya.server.player.state.PlayerStateManager;

public class AvailabilityGameInitiatorManager implements GameInitiatorManager {

    final private PlayerStateManager playerStateManager;
    final private GameInitiatorService initiatorService;

    public AvailabilityGameInitiatorManager(final PlayerStateManager playerStateManager, final GameInitiatorService initiatorService) {
        this.playerStateManager = playerStateManager;
        this.initiatorService = initiatorService;
    }

    public void register(final GameConstruction availabilityRequest) {
        // Step 1. Checking all users are active
        final Collection<Long> participants = availabilityRequest.fetchAcceptedParticipants();
        // Step 2. Checking all participants are available
        final GameInitiation initiation = new GameInitiation(availabilityRequest.getSession(), participants, availabilityRequest.getRequest()
                .getSpecification());
        if (!initiatorService.initiate(initiation)) {
            // Step 2.1 Pretty naive implementation of MessageListener functionality
            playerStateManager.subscribe(participants, new MessageListener() {

                @Override
                public void onMessage(Message message, byte[] pattern) {
                    if (initiatorService.initiate(initiation))
                        playerStateManager.unsubscribe(participants, this);
                }

            });
        }
    }

}
