package com.clemble.casino.server.game.construct;

import static com.clemble.casino.error.ClembleCasinoError.GameConstructionInsufficientMoney;
import static com.clemble.casino.error.ClembleCasinoError.GameConstructionInvalidInvitationResponse;
import static com.clemble.casino.error.ClembleCasinoError.GameConstructionInvalidRequest;
import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.player.account.ServerPlayerAccountService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class BasicServerGameConstructionService implements ServerGameConstructionService {

    final private GameIdGenerator gameIdGenerator;
    final private ServerPlayerAccountService playerAccountService;

    final private AutomaticGameConstructionManager automaticConstructionManager;
    final private AvailabilityGameConstructionManager availabilityConstructionManager;

    public BasicServerGameConstructionService(
            final GameIdGenerator gameIdGenerator,
            final ServerPlayerAccountService playerAccountService,
            final PlayerNotificationService playerNotificationService,
            final GameConstructionRepository constructionRepository,
            final ServerGameInitiationService initiatorService,
            final PlayerLockService playerLockService,
            final ServerPlayerPresenceService playerStateManager) {
        this.gameIdGenerator = checkNotNull(gameIdGenerator);
        this.playerAccountService = checkNotNull(playerAccountService);

        this.automaticConstructionManager = new AutomaticGameConstructionManager(initiatorService, constructionRepository, checkNotNull(playerLockService), playerStateManager);
        this.availabilityConstructionManager = new AvailabilityGameConstructionManager(playerAccountService, constructionRepository, playerNotificationService, initiatorService);
    }

    @Transactional
    final public GameConstruction construct(PlayerGameConstructionRequest request) {
        // Step 1. Sanity check
        if (request == null || request.getSpecification() == null)
            throw ClembleCasinoException.fromError(GameConstructionInvalidRequest);
        // Step 2. Checking players can afford operations
        // Step 2.1. Checking initiator
        Money price = request.getSpecification().getPrice();
        if (!playerAccountService.canAfford(request.getPlayer(), price))
            throw ClembleCasinoException.fromError(GameConstructionInsufficientMoney);
        // Step 3. Check ready
        
        if (request instanceof AutomaticGameRequest) {
            return automaticConstructionManager.register((AutomaticGameRequest) request, gameIdGenerator.newId());
        } else if (request instanceof AvailabilityGameRequest) {
            return availabilityConstructionManager.register((AvailabilityGameRequest) request, gameIdGenerator.newId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    final public GameConstruction invitationResponsed(InvitationResponseEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw ClembleCasinoException.fromError(GameConstructionInvalidInvitationResponse);
        // Step 2. Calling availability construction manager
        return availabilityConstructionManager.invitationResponsed(response);
    }

}
