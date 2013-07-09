package com.gogomaya.server.game.construct;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.event.schedule.GameConstructedEvent;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.game.event.schedule.InvitationDeclinedEvent;
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.game.event.schedule.PlayerInvitedEvent;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.repository.game.GameConstructionRepository;

public class AvailabilityConstructionManager implements GameConstructionManager<AvailabilityGameRequest> {
    final private GameConstructionRepository constructionRepository;
    final private GameInitiatorService initiatorService;

    final private PlayerNotificationService playerNotificationService;

    public AvailabilityConstructionManager(GameConstructionRepository constructionRepository,
            PlayerNotificationService notificationService,
            GameInitiatorService initiatorService) {
        this.constructionRepository = constructionRepository;
        this.playerNotificationService = notificationService;
        this.initiatorService = initiatorService;
    }

    @Override
    public GameConstruction register(AvailabilityGameRequest request) {
        // Step 3. Processing to opponents creation
        GameConstruction construction = new GameConstruction(request);
        construction.setState(GameConstructionState.pending);
        construction.getResponces().put(request.getPlayerId(), new InvitationAcceptedEvent(construction.getConstruction(), request.getPlayerId()));
        construction = constructionRepository.saveAndFlush(construction);
        // Step 4. Sending invitation to opponents
        if (!construction.getResponces().complete()) {
            playerNotificationService.notify(request.getParticipants(), new PlayerInvitedEvent(construction.getConstruction(), request));
        } else {
            constructionComplete(construction);
        }
        // Step 5. Returning constructed construction
        return construction;
    }

    final public GameConstruction invitationResponsed(InvitationResponceEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidInvitationResponse);
        // Step 2. Checking associated construction
        GameConstruction construction = constructionRepository.findOne(response.getConstruction());
        if (construction == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionDoesNotExistent);
        if (construction.getState() != GameConstructionState.pending)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidState);
        // Step 3. Checking if player is part of the game
        ActionLatch responseLatch = construction.getResponces();
        responseLatch.put(response.getPlayerId(), response);
        // Step 4. Checking if latch is full
        if (response instanceof InvitationDeclinedEvent) {
            // Step 4.1. In case declined send game canceled notification
            construction.setState(GameConstructionState.canceled);
        } else if (responseLatch.complete()) {
            construction = constructionComplete(construction);
        }
        construction = constructionRepository.saveAndFlush(construction);
        playerNotificationService.notify(responseLatch.fetchParticipants(), response);
        return construction;
    }

    final private GameConstruction constructionComplete(GameConstruction construction) {
        // Step 1. Updating state
        construction.setState(GameConstructionState.constructed);
        construction = constructionRepository.saveAndFlush(construction);
        // Step 2. Notifying Participants
        ActionLatch responseLatch = construction.getResponces();
        playerNotificationService.notify(responseLatch.fetchParticipants(), new GameConstructedEvent(construction.getConstruction()));
        // Step 3. Moving to the next step
        initiatorService.initiate(construction);
        return construction;
    }

}
