package com.clemble.casino.server.game.construction.availability;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.base.ActionLatch;
import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionState;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.schedule.GameConstructedEvent;
import com.clemble.casino.game.event.schedule.InvitationDeclinedEvent;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.event.schedule.PlayerInvitedEvent;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.game.service.AvailabilityGameConstructionService;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.server.repository.game.ServerGameConfigurationRepository;

public class ServerAvailabilityGameConstructionService implements AvailabilityGameConstructionService {

    final private GameIdGenerator idGenerator;
    final private GameConstructionRepository constructionRepository;
    final private PlayerNotificationService playerNotificationService;
    final private ServerPlayerAccountService playerAccountService;
    final private PendingGameInitiationEventListener pendingInitiationService;

    public ServerAvailabilityGameConstructionService(
            GameIdGenerator idGenerator,
            ServerPlayerAccountService accountServerService,
            ServerGameConfigurationRepository configurationRepository,
            GameConstructionRepository constructionRepository,
            PlayerNotificationService notificationService,
            PendingGameInitiationEventListener pendingInitiationService) {
        this.idGenerator = checkNotNull(idGenerator);
        this.playerAccountService = checkNotNull(accountServerService);
        this.constructionRepository = checkNotNull(constructionRepository);
        this.playerNotificationService = checkNotNull(notificationService);
        this.pendingInitiationService = checkNotNull(pendingInitiationService);
    }

    @Transactional
    public GameConstruction construct(AvailabilityGameRequest request) {
        // Step 1. Sanity check
        if (request == null || request.getConfiguration() == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidRequest);
        String id = idGenerator.newId();
        // Step 2. Checking players can afford operations
        // Step 2.1. Checking initiator
        Money price = request.getConfiguration().getPrice();
        // Step 2.2. Checking opponents
        if (!playerAccountService.canAfford(request.getParticipants(), price))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInsufficientMoney);
        // Step 3. Processing to opponents creation
        GameConstruction construction = new GameConstruction(id, request);
        construction = constructionRepository.saveAndFlush(construction);
        // Step 4. Sending invitation to opponents
        playerNotificationService.notify(request.getParticipants(), new PlayerInvitedEvent(construction.getSession(), request));
        // Step 5. Returning constructed construction
        return construction;
    }

    final public GameConstruction invitationResponsed(InvitationResponseEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidInvitationResponse);
        return tryReply(response);
    }


    @Override
    public PlayerAwareEvent getReply(Game game, String session, String player) {
        return constructionRepository.findOne(new GameSessionKey(game, session)).getResponses().fetchAction(player);
    }

    @Override
    public GameConstruction reply(InvitationResponseEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidInvitationResponse);
        return tryReply(response);
    }

    final private GameConstruction tryReply(InvitationResponseEvent response) {
        try {
            // Step 1. Checking associated construction
            GameConstruction construction = constructionRepository.findOne(response.getSession());
            if (construction == null)
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionDoesNotExistent);
            if (construction.getState() != GameConstructionState.pending)
                throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidState);
            // Step 2. Checking if player is part of the game
            ActionLatch responseLatch = construction.getResponses();
            responseLatch.put(response);
            // Step 3. Notifying of applied response
            construction = constructionRepository.saveAndFlush(construction);
            playerNotificationService.notify(responseLatch.fetchParticipants(), response);
            // Step 4. Checking if latch is full
            if (response instanceof InvitationDeclinedEvent) {
                // Step 4.1. In case declined send game canceled notification
                construction.setState(GameConstructionState.canceled);
                construction = constructionRepository.saveAndFlush(construction);
            } else if (responseLatch.complete()) {
                GameInitiation initiation = construction.toInitiation();
                // Step 5. Updating state
                construction.setState(GameConstructionState.constructed);
                construction = constructionRepository.saveAndFlush(construction);
                // Step 6. Notifying Participants
                playerNotificationService.notify(initiation.getParticipants(), new GameConstructedEvent(construction.getSession()));
                // Step 7. Moving to the next step
                pendingInitiationService.add(initiation);
            }
            return construction;
        } catch (ConcurrencyFailureException concurrencyFailureException) {
            return tryReply(response);
        }
    }

    @Override
    public GameConstruction getConstruction(Game game, String session) {
        return constructionRepository.findOne(new GameSessionKey(game, session));
    }

    @Override
    public Collection<GameInitiation> getPending(String player) {
        return pendingInitiationService.getPending(player);
    }

}
