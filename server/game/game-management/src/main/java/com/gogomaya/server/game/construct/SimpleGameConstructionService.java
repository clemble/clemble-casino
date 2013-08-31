package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.base.ActionLatch;
import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.game.construct.AutomaticGameRequest;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameConstructionState;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.GameConstructedEvent;
import com.gogomaya.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.game.event.schedule.InvitationDeclinedEvent;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.game.event.schedule.PlayerInvitedEvent;
import com.gogomaya.money.Money;
import com.gogomaya.server.player.account.PlayerAccountProcessingService;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.state.PlayerStateManager;
import com.gogomaya.server.repository.game.GameConstructionRepository;

public class SimpleGameConstructionService implements GameConstructionProcessingService {

    final private AutomaticConstructionManager automaticGameInitiatorManager;

    final private PlayerAccountProcessingService playerAccounttService;
    final private PlayerNotificationService playerNotificationService;
    final private GameInitiatorService initiatorService;
    final private GameConstructionRepository constructionRepository;

    public SimpleGameConstructionService(final PlayerAccountProcessingService playerAccountService,
            final PlayerNotificationService playerNotificationService,
            final GameConstructionRepository constructionRepository,
            final GameInitiatorService initiatorService,
            final PlayerLockService playerLockService,
            final PlayerStateManager playerStateManager) {
        this.initiatorService = checkNotNull(initiatorService);
        this.playerAccounttService = checkNotNull(playerAccountService);
        this.playerNotificationService = checkNotNull(playerNotificationService);
        this.constructionRepository = checkNotNull(constructionRepository);

        this.automaticGameInitiatorManager = new AutomaticConstructionManager(initiatorService, constructionRepository, checkNotNull(playerLockService),
                playerStateManager);
    }

    @Transactional
    final public GameConstruction construct(GameRequest request) {
        // Step 1. Sanity check
        if (request == null || request.getSpecification() == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidRequest);
        // Step 2. Checking players can afford operations
        // Step 2.1. Checking initiator
        Money price = request.getSpecification().getPrice();
        if (!playerAccounttService.canAfford(request.getPlayerId(), price))
            throw GogomayaException.fromError(GogomayaError.GameConstructionInsufficientMoney);
        if (request instanceof AutomaticGameRequest) {
            return automaticGameInitiatorManager.register((AutomaticGameRequest) request);
        }
        // Step 2.2. Checking opponents
        if (!playerAccounttService.canAfford(request.getParticipants(), price))
            throw GogomayaException.fromError(GogomayaError.GameConstructionInsufficientMoney);
        // Step 3. Processing to opponents creation
        GameConstruction construction = new GameConstruction(request);
        construction.setState(GameConstructionState.pending);
        construction.getResponses().put(request.getPlayerId(), new InvitationAcceptedEvent(construction.getSession(), request.getPlayerId()));
        construction = constructionRepository.saveAndFlush(construction);
        // Step 4. Sending invitation to opponents
        if (!construction.getResponses().complete()) {
            playerNotificationService.notify(request.getParticipants(), new PlayerInvitedEvent(construction.getSession(), request));
        } else {
            constructionComplete(construction);
        }
        // Step 5. Returning constructed construction
        return construction;
    }

    final public GameConstruction invitationResponsed(InvitationResponseEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidInvitationResponse);
        return tryAcceptResponce(response);
    }

    final private GameConstruction tryAcceptResponce(InvitationResponseEvent response) {
        try {
            // Step 1. Checking associated construction
            GameConstruction construction = constructionRepository.findOne(response.getSession());
            if (construction == null)
                throw GogomayaException.fromError(GogomayaError.GameConstructionDoesNotExistent);
            if (construction.getState() != GameConstructionState.pending)
                throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidState);
            // Step 2. Checking if player is part of the game
            ActionLatch responseLatch = construction.getResponses();
            responseLatch.put(response.getPlayerId(), response);
            // Step 3. Checking if latch is full
            if (response instanceof InvitationDeclinedEvent) {
                // Step 4.1. In case declined send game canceled notification
                construction.setState(GameConstructionState.canceled);
            } else if (responseLatch.complete()) {
                construction = constructionComplete(construction);
            }
            construction = constructionRepository.saveAndFlush(construction);
            playerNotificationService.notify(responseLatch.fetchParticipants(), response);
            return construction;
        } catch (ConcurrencyFailureException concurrencyFailureException) {
            return tryAcceptResponce(response);
        }
    }

    final private GameConstruction constructionComplete(GameConstruction construction) {
        // Step 1. Updating state
        construction.setState(GameConstructionState.constructed);
        construction = constructionRepository.saveAndFlush(construction);
        // Step 2. Notifying Participants
        ActionLatch responseLatch = construction.getResponses();
        playerNotificationService.notify(responseLatch.fetchParticipants(), new GameConstructedEvent(construction.getSession()));
        // Step 3. Moving to the next step
        initiatorService.initiate(construction);
        return construction;
    }

}
