package com.gogomaya.server.web.active.session;

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

import com.gogomaya.server.error.GogomayaError;
import com.gogomaya.server.error.GogomayaException;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.configuration.GameConfigurationManager;
import com.gogomaya.server.game.construct.AutomaticGameRequest;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.game.construct.GameConstructionService;
import com.gogomaya.server.game.construct.GameRequest;
import com.gogomaya.server.game.event.schedule.InvitationResponceEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.repository.game.GameConstructionRepository;
import com.gogomaya.server.web.mapping.GameWebMapping;

@Controller
public class GameConstructionController<State extends GameState> {

    final private GameConfigurationManager configurationManager;
    final private GameConstructionService constructionService;
    final private GameConstructionRepository constructionRepository;

    public GameConstructionController(final GameConstructionRepository constructionRepository,
            final GameConstructionService matchingService,
            final GameConfigurationManager configurationManager) {
        this.constructionService = checkNotNull(matchingService);
        this.configurationManager = checkNotNull(configurationManager);
        this.constructionRepository = constructionRepository;
    }

    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_CONSTRUCTION_AUTOMATIC, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    GameConstruction match(@RequestHeader("playerId") final long playerId, @RequestBody final GameSpecification specification) {
        // Step 1. Generating Instant game request
        AutomaticGameRequest instantGameRequest = new AutomaticGameRequest(playerId, specification);
        // Step 3. Invoking construction service
        return constructionService.construct(instantGameRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_CONSTRUCTION_GENERIC, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    GameConstruction construct(@RequestHeader("playerId") final long playerId, @RequestBody final GameRequest gameRequest) {
        // Step 1. Checking that provided specification was valid
        if (!configurationManager.getSpecificationOptions().valid(gameRequest.getSpecification()))
            throw GogomayaException.fromError(GogomayaError.GameSpecificationInvalid);
        // Step 2. Invoking actual matching service
        return constructionService.construct(gameRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_CONSTRUCTION_RESPONCE, produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody
    GameConstruction invitationResponsed(@RequestHeader("playerId") final long playerId, @RequestBody final InvitationResponceEvent gameRequest) {
        // Step 1. Invoking actual matching service
        return constructionService.invitationResponsed(gameRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_CONSTRUCTION, produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody
    GameConstruction getConstruct(@RequestHeader("playerId") final long playerId, @PathVariable("constructionId") final long constructionId) {
        // Step 1. Searching for construction
        GameConstruction construction = constructionRepository.findOne(constructionId);
        // Step 2. Sending error in case resource not found
        if (construction == null)
            throw GogomayaException.fromError(GogomayaError.GameConstructionDoesNotExistent);
        // Step 3. Returning construction
        return construction;
    }
}
