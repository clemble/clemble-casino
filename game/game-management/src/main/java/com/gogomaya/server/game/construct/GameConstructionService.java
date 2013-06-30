package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.ActionLatch;
import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.event.schedule.GameCanceledEvent;
import com.gogomaya.server.game.event.schedule.GameConstructedEvent;
import com.gogomaya.server.game.event.schedule.InvitationAcceptedEvent;
import com.gogomaya.server.game.event.schedule.InvitationDeclinedEvent;
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.game.event.schedule.PlayerInvitedEvent;
import com.gogomaya.server.money.Money;
import com.gogomaya.server.player.lock.PlayerLockService;
import com.gogomaya.server.player.notification.PlayerNotificationService;
import com.gogomaya.server.player.wallet.WalletTransactionManager;

public class GameConstructionService {

    final private AutomaticGameInitiatorManager automaticGameInitiatorManager;

    final private WalletTransactionManager walletTransactionManager;
    final private PlayerNotificationService playerNotificationService;
    final private GameInitiatorService initiatorService;
    final private GameConstructionRepository constructionRepository;

    public GameConstructionService(final WalletTransactionManager walletTransactionManager, final PlayerNotificationService playerNotificationService,
            final GameConstructionRepository constructionRepository, final GameInitiatorService initiatorService, final PlayerLockService playerLockService) {
        this.initiatorService = checkNotNull(initiatorService);
        this.walletTransactionManager = checkNotNull(walletTransactionManager);
        this.playerNotificationService = checkNotNull(playerNotificationService);
        this.constructionRepository = checkNotNull(constructionRepository);

        this.automaticGameInitiatorManager = new AutomaticGameInitiatorManager(initiatorService, constructionRepository, checkNotNull(playerLockService));
    }

    final public GameConstruction construct(GameRequest request) {
        // Step 1. Sanity check
        if (request == null || request.getSpecification() == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidRequest);
        // Step 2. Checking players can afford operations
        // Step 2.1. Checking initiator
        Money ammountNeeded = request.getSpecification().extractMoneyNeeded();
        if (!walletTransactionManager.canAfford(request.getPlayerId(), ammountNeeded))
            throw GogomayaException.fromError(GogomayaError.GameConstructionInsufficientMoney);
        if (request instanceof AutomaticGameRequest) {
            return automaticGameInitiatorManager.register((AutomaticGameRequest) request);
        }
        // Step 2.2. Checking opponents
        if (!walletTransactionManager.canAfford(request.getParticipants(), ammountNeeded))
            throw GogomayaException.fromError(GogomayaError.GameConstructionInsufficientMoney);
        // Step 3. Processing to opponents creation
        GameConstruction construction = new GameConstruction(request);
        construction = constructionRepository.saveAndFlush(construction);
        construction.getResponces().put(request.getPlayerId(), new InvitationAcceptedEvent(request.getPlayerId(), construction.getConstruction()));
        // Step 4. Sending invitation to opponents
        if (!construction.getResponces().complete()) {
            playerNotificationService.notify(request.getParticipants(), new PlayerInvitedEvent(construction.getConstruction(), request));
        } else {
            constructionComplete(construction);
        }
        // Step 5. Returning constructed construction
        return construction;
    }

    final public void invitationResponsed(InvitationResponceEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidInvitationResponse);
        // Step 2. Checking associated construction
        GameConstruction construction = constructionRepository.findOne(response.getConstruction());
        if (construction == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionNonExistent);
        if (construction.getState() != GameConstructionState.pending)
            throw GogomayaException.fromError(GogomayaError.GameConstructionInvalidState);
        // Step 3. Checking if player is part of the game
        ActionLatch<InvitationResponceEvent> action = construction.getResponces();
        action.put(response.getPlayerId(), response);
        // Step 4. Checking if latch is full
        if (response instanceof InvitationDeclinedEvent) {
            // Step 4.1. In case declined send game canceled notification
            construction.setState(GameConstructionState.canceled);
            constructionRepository.saveAndFlush(construction);
            playerNotificationService.notify(action.getParticipants(), new GameCanceledEvent(response.getConstruction(), response.getPlayerId()));
        } else if (action.complete()) {
            constructionComplete(construction);
        }
    }

    final private void constructionComplete(GameConstruction construction) {
        // Step 1. Updating state
        construction.setState(GameConstructionState.constructed);
        constructionRepository.saveAndFlush(construction);
        // Step 2. Notifying Participants
        ActionLatch<InvitationResponceEvent> action = construction.getResponces();
        playerNotificationService.notify(action.getParticipants(), new GameConstructedEvent(construction.getConstruction()));
        // Step 3. Moving to the next step
        initiatorService.initiate(construction);
    }

}
