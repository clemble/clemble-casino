package com.clemble.casino.server.game.construction.availability;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.error.ClembleCasinoFailure;
import com.clemble.casino.payment.service.PlayerAccountServiceContract;
import com.clemble.casino.server.game.GameSessionKeyGenerator;
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
import com.clemble.casino.game.service.AvailabilityGameConstructionService;
import com.clemble.casino.money.Money;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.game.repository.GameConstructionRepository;
import com.clemble.casino.server.game.repository.ServerGameConfigurationRepository;

public class ServerAvailabilityGameConstructionService implements AvailabilityGameConstructionService {

    final private GameSessionKeyGenerator sessionKeyGenerator;
    final private GameConstructionRepository constructionRepository;
    final private PlayerNotificationService playerNotificationService;
    final private PlayerAccountServiceContract accountService;
    final private PendingGameInitiationEventListener pendingInitiationService;

    public ServerAvailabilityGameConstructionService(
            GameSessionKeyGenerator sessionKeyGenerator,
            PlayerAccountServiceContract accountServerService,
            ServerGameConfigurationRepository configurationRepository,
            GameConstructionRepository constructionRepository,
            PlayerNotificationService notificationService,
            PendingGameInitiationEventListener pendingInitiationService) {
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
        GameConstruction construction = new GameConstruction(sessionKeyGenerator.generate(request.getConfiguration()), request);
        construction = constructionRepository.saveAndFlush(construction);
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
    public PlayerAwareEvent getReply(Game game, String session, String player) {
        return constructionRepository.findOne(new GameSessionKey(game, session)).getResponses().filterAction(player);
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
            GameConstruction construction = constructionRepository.findOne(response.getSessionKey());
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
                playerNotificationService.notify(initiation.getParticipants(), new GameConstructedEvent(construction.getSessionKey()));
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
