package com.clemble.casino.server.game.aspect.management;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.construct.ManagerGameConstructionRequest;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.server.game.aspect.GameManagementAspect;
import com.clemble.casino.server.game.construct.GameInitiatorService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class  GameSequenceManagementAspect implements GameManagementAspect {

    final private GameIdGenerator idGenerator;
    final private GameConstructionRepository constructionRepository;
    final private GameInitiatorService initiatorService;

    public GameSequenceManagementAspect(GameIdGenerator idGenerator,
            GameInitiatorService initiatorService,
            GameConstructionRepository constructionRepository) {
        this.initiatorService = checkNotNull(initiatorService);
        this.idGenerator = checkNotNull(idGenerator);
        this.constructionRepository = checkNotNull(constructionRepository);
    }

    @Override
    public <State extends GameState> void afterGame(GameSession<State> session) {
        GameSessionKey sessionKey = session.getSession();
        Collection<String> participants = session.getPlayers();
        GameSpecification specification = session.getSpecification();
        // Step 1. Generating game construction
        GameConstruction construction = new GameConstruction(new ManagerGameConstructionRequest(participants, sessionKey, specification));
        construction.setSession(new GameSessionKey(specification.getName().getGame(), idGenerator.newId()));
        // Step 2. Saving game construction
        construction = constructionRepository.saveAndFlush(construction);
        // Step 3. Initiating new game
        GameInitiation initiation = new GameInitiation(construction.getSession(), participants, specification);
        initiatorService.initiate(initiation);
    }

}
