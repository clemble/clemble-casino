package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.construct.AvailabilityGameRequest;
import com.gogomaya.server.game.construct.InstantGameRequest;
import com.gogomaya.server.game.specification.GameSpecification;

public class GameConstructionService<State extends GameState> {

    final private InstantGameConstructor<State> instantGameConstructor;
    final private AvailabilityGameConstructor<State> availabilityGameConstructor;

    public GameConstructionService(InstantGameConstructor<State> instantGameConstructor, AvailabilityGameConstructor<State> availabilityGameConstructor) {
        this.instantGameConstructor = checkNotNull(instantGameConstructor);
        this.availabilityGameConstructor = checkNotNull(availabilityGameConstructor);
    }

    public GameTable<State> instantGame(final long playerId, final GameSpecification specification) {
        InstantGameRequest instantGameRequest = new InstantGameRequest();
        instantGameRequest.setSpecification(specification);
        instantGameRequest.setPlayerId(playerId);

        return instantGameConstructor.construct(instantGameRequest);
    }

    public GameTable<State> avilabilityGame(final long playerId, final Collection<Long> opponents, final GameSpecification specification) {
        AvailabilityGameRequest availabilityGameRequest = new AvailabilityGameRequest();
        availabilityGameRequest.setOpponents(opponents);
        availabilityGameRequest.setPlayerId(playerId);
        availabilityGameRequest.setSpecification(specification);

        return availabilityGameConstructor.construct(availabilityGameRequest);
    }

}
