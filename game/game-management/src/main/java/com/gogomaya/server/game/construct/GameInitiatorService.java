package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.action.GameSessionProcessor;
import com.gogomaya.server.player.state.PlayerStateManager;

public class GameInitiatorService {

    final private GameSessionProcessor<?> processor;
    final private GameConstructionRepository constructionRepository;

    final private AvailabilityGameInitiatorManager availabilityGameInitiatorManager;

    public GameInitiatorService(final GameSessionProcessor<?> stateFactory, final GameConstructionRepository constructionRepository,
            final PlayerStateManager playerStateManager) {
        this.processor = checkNotNull(stateFactory);
        this.constructionRepository = checkNotNull(constructionRepository);

        this.availabilityGameInitiatorManager = new AvailabilityGameInitiatorManager(playerStateManager, this);
    }

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

    public GameTable<?> initiate(GameInitiation initiation) {
        GameTable<?> table = processor.start(initiation);

        constructionRepository.specifySession(initiation.getConstruction(), table.getCurrentSession().getSession());

        return table;
    }
}
