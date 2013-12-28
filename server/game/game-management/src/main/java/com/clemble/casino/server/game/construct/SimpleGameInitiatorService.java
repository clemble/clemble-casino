package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionRequest;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.player.PlayerAwareUtils;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.player.presence.SystemNotificationServiceListener;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

public class SimpleGameInitiatorService implements GameInitiatorService {

    final private Logger LOG = LoggerFactory.getLogger(SimpleGameInitiatorService.class);

    final private PlayerPresenceServerService presenceService;

    final private GameSessionProcessor<?> processor;

    final private AvailabilityGameInitiatorManager availabilityGameInitiatorManager;

    public SimpleGameInitiatorService(final GameSessionProcessor<?> stateFactory,
            final PlayerPresenceServerService presenceService,
            final SystemNotificationServiceListener playerPresenceService) {
        this.processor = checkNotNull(stateFactory);
        this.presenceService = checkNotNull(presenceService);

        this.availabilityGameInitiatorManager = new AvailabilityGameInitiatorManager(checkNotNull(playerPresenceService), this);
    }

    @Override
    public void initiate(GameConstruction construction) {
        GameConstructionRequest request = construction.getRequest();
        if (request instanceof AutomaticGameRequest) {
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerCriticalError);
        } else if (request instanceof AvailabilityGameRequest) {
            availabilityGameInitiatorManager.register(construction);
        } else {
            throw ClembleCasinoException.fromError(ClembleCasinoError.ServerCriticalError);
        }
    }

    @Override
    public boolean initiate(GameInitiation initiation) {
        if (presenceService.markPlaying(PlayerAwareUtils.toPlayerList(initiation.getParticipants()), initiation.getSession())) {
            LOG.trace("Successfully updated presences, starting a new game");
            processor.start(initiation);
            return true;
        }
        LOG.trace("Failed to update presences");
        return false;
    }

}
