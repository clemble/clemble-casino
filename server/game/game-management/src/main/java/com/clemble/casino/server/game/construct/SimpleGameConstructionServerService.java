package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionState;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.game.event.schedule.GameConstructedEvent;
import com.clemble.casino.game.event.schedule.InvitationAcceptedEvent;
import com.clemble.casino.game.event.schedule.InvitationDeclinedEvent;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.event.schedule.PlayerInvitedEvent;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class SimpleGameConstructionServerService implements GameConstructionServerService {

    final private AutomaticConstructionManager automaticGameInitiatorManager;

    final private GameIdGenerator gameIdGenerator;
    final private PlayerAccountServerService playerAccountService;
    final private PlayerNotificationService<Event> playerNotificationService;
    final private GameInitiatorService initiatorService;
    final private GameConstructionRepository constructionRepository;

    public SimpleGameConstructionServerService(
            final GameIdGenerator gameIdGenerator,
            final PlayerAccountServerService playerAccountService,
            final PlayerNotificationService<Event> playerNotificationService,
            final GameConstructionRepository constructionRepository,
            final GameInitiatorService initiatorService,
            final PlayerLockService playerLockService,
            final PlayerPresenceServerService playerStateManager) {
        this.gameIdGenerator = checkNotNull(gameIdGenerator);
        this.initiatorService = checkNotNull(initiatorService);
        this.playerAccountService = checkNotNull(playerAccountService);
        this.playerNotificationService = checkNotNull(playerNotificationService);
        this.constructionRepository = checkNotNull(constructionRepository);

        this.automaticGameInitiatorManager = new AutomaticConstructionManager(initiatorService, constructionRepository, checkNotNull(playerLockService),
                playerStateManager);
    }

    @Transactional
    final public GameConstruction construct(GameRequest request) {
        // Step 1. Sanity check
        if (request == null || request.getSpecification() == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidRequest);
        // Step 2. Checking players can afford operations
        // Step 2.1. Checking initiator
        Money price = request.getSpecification().getPrice();
        if (!playerAccountService.canAfford(request.getPlayer(), price))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInsufficientMoney);
        if (request instanceof AutomaticGameRequest) {
            return automaticGameInitiatorManager.register((AutomaticGameRequest) request);
        }
        // Step 2.2. Checking opponents
        if (!playerAccountService.canAfford(request.getParticipants(), price))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInsufficientMoney);
        // Step 3. Processing to opponents creation
        GameConstruction construction = new GameConstruction(request);
        construction.setSession(new GameSessionKey(request.getSpecification().getName().getGame(), gameIdGenerator.newId()));
        construction.setState(GameConstructionState.pending);
        construction.getResponses().put(request.getPlayer(), new InvitationAcceptedEvent(construction.getSession(), request.getPlayer()));
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
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidInvitationResponse);
        return tryAcceptResponce(response);
    }

    final private GameConstruction tryAcceptResponce(InvitationResponseEvent response) {
        try {
            // Step 1. Checking associated construction
            GameConstruction construction = constructionRepository.findOne(response.getSession());
            if (construction == null)
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionDoesNotExistent);
            if (construction.getState() != GameConstructionState.pending)
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidState);
            // Step 2. Checking if player is part of the game
            ActionLatch responseLatch = construction.getResponses();
            responseLatch.put(response.getPlayer(), response);
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
