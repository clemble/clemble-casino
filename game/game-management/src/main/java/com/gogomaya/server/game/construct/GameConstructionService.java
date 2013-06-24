package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;

public class GameConstructionService<State extends GameState> {


    final private InstantGameConstructor<State> instantGameConstructor;
    final private AvailabilityGameConstructor<State> availabilityGameConstructor;

    public GameConstructionService(InstantGameConstructor<State> instantGameConstructor, AvailabilityGameConstructor<State> availabilityGameConstructor) {
        this.instantGameConstructor = checkNotNull(instantGameConstructor);
        this.availabilityGameConstructor = checkNotNull(availabilityGameConstructor);
    }

    public GameTable<State> construct(final GameRequest gameRequest) {
        if(gameRequest instanceof InstantGameRequest) {
            return instantGameConstructor.construct((InstantGameRequest) gameRequest);
        } else if(gameRequest instanceof AvailabilityGameRequest) {
            return availabilityGameConstructor.construct((AvailabilityGameRequest) gameRequest);
        } else {
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        }

    }

}
