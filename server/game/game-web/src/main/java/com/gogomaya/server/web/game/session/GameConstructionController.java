package com.gogomaya.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.gogomaya.event.ClientEvent;
import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.game.construct.GameRequest;
import com.gogomaya.game.event.schedule.InvitationResponseEvent;
import com.gogomaya.server.game.configuration.GameSpecificationRegistry;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.web.game.GameWebMapping;

@Controller
public class GameConstructionController<State extends GameState> {

    final private GameSpecificationRegistry configurationManager;
    final private GameConstructionService constructionService;
    final private GameConstructionRepository constructionRepository;

    public GameConstructionController(final GameConstructionRepository constructionRepository,
            final GameConstructionService matchingService,
            final GameSpecificationRegistry configurationManager) {
        this.constructionService = checkNotNull(matchingService);
        this.configurationManager = checkNotNull(configurationManager);
        this.constructionRepository = checkNotNull(constructionRepository);
    }

    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_SESSIONS, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    GameConstruction construct(@RequestHeader("playerId") final long playerId, @RequestBody final GameRequest gameRequest) {
        // Step 1. Checking that provided specification was valid
        if (!configurationManager.getSpecificationOptions(gameRequest.getSpecification().getName().getGame()).valid(gameRequest.getSpecification()))
            throw GogomayaException.fromError(GogomayaError.GameSpecificationInvalid);
        // Step 2. Invoking actual matching service
        return constructionService.construct(gameRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SESSIONS_CONSTRUCTION, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    GameConstruction getConstruct(@RequestHeader("playerId") final long playerId, @PathVariable("sessionId") final long session) {
        // Step 1. Searching for construction
        GameConstruction construction = constructionRepository.findOne(session);
        // Step 2. Sending error in case resource not found
        if (construction == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionDoesNotExistent);
        // Step 3. Returning construction
        return construction;
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES_PLAYER, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    ClientEvent getResponce(
            @RequestHeader("playerId") final long requester,
            @PathVariable("sessionId") final long session,
            @PathVariable("playerId") final long player) {
        return (ClientEvent) constructionRepository.findOne(session).getResponses().fetchAction(player);
    }

    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_SESSIONS_CONSTRUCTION_RESPONSES, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    GameConstruction invitationResponsed(
            @RequestHeader("playerId") final long playerId,
            @PathVariable("sessionId") long sessionId,
            @RequestBody final InvitationResponseEvent gameRequest) {
        // Step 1. Invoking actual matching service
        return constructionService.invitationResponsed(gameRequest);
    }
}