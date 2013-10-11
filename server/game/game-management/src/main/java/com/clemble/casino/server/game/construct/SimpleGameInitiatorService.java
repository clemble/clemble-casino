package com.clemble.casino.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.error.GogomayaError;
import com.clemble.casino.error.GogomayaException;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.construct.GameRequest;
import com.clemble.casino.server.game.action.GameSessionProcessor;
import com.clemble.casino.server.player.presence.PlayerPresenceServerService;

public class SimpleGameInitiatorService implements GameInitiatorService {

    final private PlayerPresenceServerService playerStateManager;

    final private GameSessionProcessor<?> processor;

    final private AvailabilityGameInitiatorManager availabilityGameInitiatorManager;

    public SimpleGameInitiatorService(final GameSessionProcessor<?> stateFactory,
            final PlayerPresenceServerService playerPresenceService) {
        this.processor = checkNotNull(stateFactory);
        this.playerStateManager = checkNotNull(playerPresenceService);

        this.availabilityGameInitiatorManager = new AvailabilityGameInitiatorManager(checkNotNull(playerPresenceService), this);
    }

    @Override
    public void initiate(GameConstruction construction) {
        GameRequest request = construction.getRequest();
        if (request instanceof AutomaticGameRequest) {
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        } else if (request instanceof AvailabilityGameRequest) {
            availabilityGameInitiatorManager.register(construction);
        } else {
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        }
    }

    @Override
    public boolean initiate(GameInitiation initiation) {
        if (playerStateManager.markPlaying(initiation.getParticipants(), initiation.getSession())) {
            processor.start(initiation);
            return true;
        }
        return false;
    }

}
