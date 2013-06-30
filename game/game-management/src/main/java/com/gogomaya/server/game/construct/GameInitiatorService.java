package com.gogomaya.server.game.construct;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameTable;
import com.gogomaya.server.game.action.GameSessionProcessor;

public class GameInitiatorService {

    final private GameSessionProcessor<?> processor;
    final private GameConstructionRepository constructionRepository;

    public GameInitiatorService(GameSessionProcessor<?> stateFactory, GameConstructionRepository constructionRepository) {
        this.processor = checkNotNull(stateFactory);
        this.constructionRepository = checkNotNull(constructionRepository);
    }

    public void initiate(GameConstruction construction) {
        GameRequest request = construction.getRequest();
        if (request instanceof AutomaticGameRequest) {
            throw GogomayaException.fromError(GogomayaError.ServerCriticalError);
        } else if (request instanceof InstantGameRequest) {
            InstantGameRequest instantGameRequest = (InstantGameRequest) request;
            initiate(new GameInitiation(construction.getConstruction(), construction.getAcceptedParticipants(), instantGameRequest.getSpecification()));
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
