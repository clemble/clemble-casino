package com.gogomaya.server.game.construct;

import java.util.Collection;

import com.gogomaya.server.player.state.PlayerStateManager;

public class AvailabilityGameInitiatorManager {

    final private PlayerStateManager playerStateManager;
    final private GameInitiatorService initiatorService;

    public AvailabilityGameInitiatorManager(final PlayerStateManager playerStateManager, final GameInitiatorService initiatorService) {
        this.playerStateManager = playerStateManager;
        this.initiatorService = initiatorService;
    }

    public void register(GameConstruction availabilityRequest) {
        // Step 1. Checking all users are active
        Collection<Long> participants = availabilityRequest.fetchAcceptedParticipants();
        // Step 2. Checking all participants are available
        if (playerStateManager.areAvailable(participants)) {
            initiatorService.initiate(new GameInitiation(availabilityRequest.getConstruction(), participants, availabilityRequest.getRequest()
                    .getSpecification()));
        }
    }
}
