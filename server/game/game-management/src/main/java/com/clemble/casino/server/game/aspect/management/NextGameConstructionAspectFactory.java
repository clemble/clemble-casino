package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;
import com.clemble.casino.server.game.construct.GameInitiatorService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class NextGameConstructionAspectFactory implements GameAspectFactory<GameEndedEvent<?>> {

    final private GameIdGenerator idGenerator;
    final private GameConstructionRepository constructionRepository;
    final private GameInitiatorService initiatorService;

    public NextGameConstructionAspectFactory(
            GameIdGenerator idGenerator,
            GameInitiatorService initiatorService,
            GameConstructionRepository constructionRepository) {
        this.initiatorService = checkNotNull(initiatorService);
        this.idGenerator = checkNotNull(idGenerator);
        this.constructionRepository = checkNotNull(constructionRepository);
    }

    @Override
    public GameAspect<GameEndedEvent<?>> construct(GameInitiation initiation, GameContext construction) {
        return new NextGameConstructionAspect(idGenerator, initiatorService, constructionRepository, initiation);
    }

}
