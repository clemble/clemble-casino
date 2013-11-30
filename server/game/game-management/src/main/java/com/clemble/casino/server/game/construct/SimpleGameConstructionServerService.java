package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.transaction.annotation.Transactional;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.payment.money.Money;
import com.clemble.casino.server.player.account.PlayerAccountServerService;
import com.clemble.casino.server.player.lock.PlayerLockService;
import com.clemble.casino.server.player.notification.PlayerNotificationService;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class SimpleGameConstructionServerService implements GameConstructionServerService {

    final private AutomaticConstructionManager automaticGameInitiatorManager;
    final private AvailabilityGameConstructionManager availabilityConstructionManager;

    final private GameIdGenerator gameIdGenerator;
    final private PlayerAccountServerService playerAccountService;

    public SimpleGameConstructionServerService(final GameIdGenerator gameIdGenerator, final PlayerAccountServerService playerAccountService,
            final PlayerNotificationService<Event> playerNotificationService, final GameConstructionRepository constructionRepository,
            final GameInitiatorService initiatorService, final PlayerLockService playerLockService, final PlayerPresenceServerService playerStateManager) {
        this.gameIdGenerator = checkNotNull(gameIdGenerator);
        this.playerAccountService = checkNotNull(playerAccountService);

        this.automaticGameInitiatorManager = new AutomaticConstructionManager(initiatorService, constructionRepository, checkNotNull(playerLockService), playerStateManager);
        this.availabilityConstructionManager = new AvailabilityGameConstructionManager(playerAccountService, constructionRepository, playerNotificationService, initiatorService);
    }

    @Transactional
    final public GameConstruction construct(PlayerGameConstructionRequest request) {
        // Step 1. Sanity check
        if (request == null || request.getSpecification() == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidRequest);
        // Step 2. Checking players can afford operations
        // Step 2.1. Checking initiator
        Money price = request.getSpecification().getPrice();
        if (!playerAccountService.canAfford(request.getPlayer(), price))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInsufficientMoney);
        if (request instanceof AutomaticGameRequest) {
            return automaticGameInitiatorManager.register((AutomaticGameRequest) request, gameIdGenerator.newId());
        } else if (request instanceof AvailabilityGameRequest) {
            return availabilityConstructionManager.register((AvailabilityGameRequest) request, gameIdGenerator.newId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    final public GameConstruction invitationResponsed(InvitationResponseEvent response) {
        // Step 1. Sanity check
        if (response == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionInvalidInvitationResponse);
        // Step 2. Calling availability construction manager
        return availabilityConstructionManager.invitationResponsed(response);
    }

}
