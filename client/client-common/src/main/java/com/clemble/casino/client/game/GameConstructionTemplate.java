package com.clemble.casino.client.game;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.client.event.ConstructionEventSelector;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.event.ClientEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.configuration.GameSpecificationOptions;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.game.event.schedule.InvitationAcceptedEvent;
import com.clemble.casino.game.event.schedule.InvitationDeclinedEvent;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.game.service.GameSpecificationService;
import com.clemble.casino.game.specification.GameSpecification;

public class GameConstructionTemplate<T extends GameState> implements GameConstructionOperations<T> {

    /**
     * Generated 12/11/13
     */
    private static final long serialVersionUID = -7073347007265754892L;

    final private String player;

    final private Game game;
    final private GameActionOperationsFactory actionOperationFactory;
    final private EventListenerOperations listenersManager;
    final private GameSpecificationService specificationService;
    final private GameConstructionService constructionService;

    public GameConstructionTemplate(String player, Game game, GameActionOperationsFactory actionOperations, GameConstructionService constructionService,
            GameSpecificationService specificationService, EventListenerOperations listenersManager) {
        this.player = checkNotNull(player);
        this.game = checkNotNull(game);
        this.actionOperationFactory = checkNotNull(actionOperations);
        this.constructionService = checkNotNull(constructionService);
        this.specificationService = checkNotNull(specificationService);
        this.listenersManager = checkNotNull(listenersManager);
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public GameSpecificationOptions get() {
        return specificationService.getSpecificationOptions(player, game);
    }

    @Override
    public GameConstruction construct(PlayerGameConstructionRequest gameRequest) {
        return constructionService.construct(gameRequest);
    }

    @Override
    public GameConstruction constructAutomatch(GameSpecification specification) {
        // Step 1. Constructing automatic request
        PlayerGameConstructionRequest automaticGameRequest = new AutomaticGameRequest(player, specification);
        // Step 2. Making actual construction
        return construct(automaticGameRequest);
    }

    @Override
    public GameConstruction constructAvailability(GameSpecification specification, Collection<String> participants) {
        // Step 1. Constructing availability request
        PlayerGameConstructionRequest availabilityGameRequest = new AvailabilityGameRequest(player, specification, participants);
        // Step 2. Making actual construction
        return construct(availabilityGameRequest);
    }

    @Override
    public GameConstruction getConstruct(String session) {
        return constructionService.getConstruct(game, session);
    }

    @Override
    public GameConstruction accept(String session) {
        return response(session, new InvitationAcceptedEvent(player, toSessionKey(session)));
    }

    @Override
    public GameConstruction decline(String session) {
        return response(session, new InvitationDeclinedEvent(player, toSessionKey(session)));
    }

    @Override
    public GameConstruction response(String session, InvitationResponseEvent responce) {
        return constructionService.reply(game, session, responce);
    }

    @Override
    public ClientEvent getResponce(String session, String fromPlayer) {
        return constructionService.getResponce(game, session, fromPlayer);
    }

    @Override
    public void subscribe(String session, EventListener constructionListener) {
        listenersManager.subscribe(new ConstructionEventSelector(toSessionKey(session)), constructionListener);
    }

    @Override
    public GameActionOperations<T> getActionOperations(String session) {
        return actionOperationFactory.construct(toSessionKey(session));
    }

    private GameSessionKey toSessionKey(String session) {
        return new GameSessionKey(game, session);
    }

}
