package com.clemble.casino.server.game.aspect.management;

import java.util.Collection;
import java.util.List;

import org.springframework.core.Ordered;

import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.game.GamePlayerRole;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameConstructionRequest;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.construct.ManagerGameConstructionRequest;
import com.clemble.casino.game.event.server.GameEndedEvent;
import com.clemble.casino.game.id.GameIdGenerator;
import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.server.game.aspect.BasicGameAspect;
import com.clemble.casino.server.game.construct.GameInitiatorService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;

public class NextGameConstructionAspect extends BasicGameAspect<GameEndedEvent<?>> {

    final private GameIdGenerator idGenerator;
    final private GameConstructionRepository constructionRepository;
    final private GameInitiatorService initiatorService;

    final private GameSpecification specification;
    final private Collection<GamePlayerRole> participants;

    public NextGameConstructionAspect(GameIdGenerator idGenerator,
            GameInitiatorService initiatorService,
            GameConstructionRepository constructionRepository,
            GameInitiation initiation) {
        super(new EventTypeSelector(GameEndedEvent.class));
        this.idGenerator = idGenerator;
        this.constructionRepository = constructionRepository;
        this.initiatorService = initiatorService;
        this.specification = initiation.getSpecification();
        this.participants = initiation.getParticipants();
    }

    @Override
    public void doEvent(GameEndedEvent<?> event) {
        GameSessionKey sessionKey = event.getSession();
        // Step 1. Generating game construction
        List<GamePlayerRole> rotatedRoles = GamePlayerRole.rotate(participants);
        GameConstructionRequest request = new ManagerGameConstructionRequest(rotatedRoles, sessionKey, specification);
        GameConstruction construction = new GameConstruction(request);
        construction.setSession(new GameSessionKey(specification.getName().getGame(), idGenerator.newId()));
        // Step 2. Saving game construction
        construction = constructionRepository.saveAndFlush(construction);
        // Step 3. Initiating new game
        GameInitiation initiation = new GameInitiation(construction.getSession(), specification, participants);
        initiatorService.initiate(initiation);
    }

}
