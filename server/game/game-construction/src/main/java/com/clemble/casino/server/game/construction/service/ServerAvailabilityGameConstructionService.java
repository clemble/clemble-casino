package com.clemble.casino.server.game.construction.service;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.base.ActionLatchService;
import com.clemble.casino.construction.ConstructionState;
import com.clemble.casino.error.ClembleCasinoFailure;
import com.clemble.casino.payment.service.PlayerAccountServiceContract;
import com.clemble.casino.player.event.PlayerEvent;
import com.clemble.casino.server.game.construction.GameSessionKeyGenerator;
import com.clemble.casino.server.game.construction.listener.PendingGameInitiationEventListener;
import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.ActionLatch;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construction.AvailabilityGameRequest;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.construction.event.GameConstructedEvent;
import com.clemble.casino.game.construction.event.InvitationDeclinedEvent;
import com.clemble.casino.game.construction.event.InvitationResponseEvent;
import com.clemble.casino.game.construction.event.PlayerInvitedEvent;
import com.clemble.casino.game.construction.service.AvailabilityGameConstructionService;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.player.notification.PlayerNotificationService;

public class ServerAvailabilityGameConstructionService implements AvailabilityGameConstructionService {

    final private ActionLatchService latchService;
    final private GameSessionKeyGenerator sessionKeyGenerator;
    final private GameConstructionRepository constructionRepository;
    final private PlayerNotificationService playerNotificationService;
    final private PlayerAccountServiceContract accountService;
    final private PendingGameInitiationService pendingInitiationService;

    public ServerAvailabilityGameConstructionService(
            ActionLatchService latchService,
            GameSessionKeyGenerator sessionKeyGenerator,
            PlayerAccountServiceContract accountServerService,
            GameConstructionRepository constructionRepository,
            PlayerNotificationService notificationService,
            PendingGameInitiationService pendingInitiationService) {
        this.latchService = latchService;
        this.sessionKeyGenerator = checkNotNull(sessionKeyGenerator);
        this.accountService = checkNotNull(accountServerService);
        this.constructionRepository = checkNotNull(constructionRepository);
        this.playerNotificationService = checkNotNull(notificationService);
        this.pendingInitiationService = checkNotNull(pendingInitiationService);
    }

    @Transactional
    public GameConstruction construct(AvailabilityGameRequest request) {
        // Step 1. Sanity check
        if (request == null || request.getConfiguration() == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidRequest);
        //String id = sessionKeyGenerator.generate();
        // Step 2. Checking players can afford operations
        // Step 2.1. Checking initiator
        Money price = request.getConfiguration().getPrice();
        // Step 2.2. Checking opponents
        Collection<String> players = accountService.canAfford(request.getParticipants(), price.getCurrency(), price.getAmount());
        if (!players.isEmpty())
            throw ClembleCasinoException.fromFailures(ClembleCasinoFailure.construct(ClembleCasinoError.GameConstructionInsufficientMoney, players));
        // Step 3. Processing to opponents creation
        GameConstruction construction = request.toConstruction(sessionKeyGenerator.generate(request.getConfiguration()));
        construction = constructionRepository.save(construction);
        latchService.save(construction.getSessionKey(), construction.getResponses());
        // Step 4. Sending invitation to opponents
        playerNotificationService.notify(request.getParticipants(), new PlayerInvitedEvent(construction.getSessionKey(), request));
        // Step 5. Returning constructed construction
        return construction;
    }

    final public GameConstruction invitationResponded(InvitationResponseEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidInvitationResponse);
        return tryReply(response);
    }


    @Override
    public PlayerEvent getReply(String sessionKey, String player) {
        return constructionRepository.findOne(sessionKey).getResponses().filterAction(player);
    }

    @Override
    public GameConstruction reply(InvitationResponseEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidInvitationResponse, response.getPlayer(), response.getSessionKey());
        return tryReply(response);
    }

    final private GameConstruction tryReply(InvitationResponseEvent response) {
        try {
            // Step 1. Checking associated construction
            GameConstruction construction = constructionRepository.findOne(response.getSessionKey());
            if (construction == null)
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionDoesNotExistent);
            if (construction.getState() != ConstructionState.pending)
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidState, response.getPlayer(), response.getSessionKey());
            // Step 2. Checking if player is part of the game
            ActionLatch responseLatch = latchService.update(response.getSessionKey(), response);
            // Step 3. Notifying of applied response
            playerNotificationService.notify(responseLatch.fetchParticipants(), response);
            construction = construction.cloneWithResponses(responseLatch);
            // Step 4. Checking if latch is full
            if (response instanceof InvitationDeclinedEvent) {
                // Step 4.1. In case declined send game canceled notification
                construction = construction.cloneWithState(ConstructionState.canceled);
            } else if (responseLatch.complete()) {
                GameInitiation initiation = construction.toInitiation();
                // Step 5. Updating state
                construction = construction.cloneWithState(ConstructionState.constructed);
                // Step 6. Notifying Participants
                playerNotificationService.notify(initiation.getParticipants(), new GameConstructedEvent(construction.getSessionKey()));
                // Step 7. Moving to the next step
                pendingInitiationService.add(initiation);
            }
            construction = constructionRepository.save(construction);
            return construction;
        } catch (ConcurrencyFailureException concurrencyFailureException) {
            return tryReply(response);
        }
    }

    @Override
    public GameConstruction getConstruction(String sessionKey) {
        return constructionRepository.findOne(sessionKey);
    }

    @Override
    public Collection<GameConstruction> getPending(String player) {
        return constructionRepository.findByParticipants(player);
    }

}