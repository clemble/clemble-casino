package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.game.construct.AutomaticGameRequest;
import com.gogomaya.game.construct.AvailabilityGameRequest;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameInitiation;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;

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
