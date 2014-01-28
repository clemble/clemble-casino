package com.clemble.casino.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.PlayerGameConstructionRequest;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.configuration.ServerGameConfigurationService;
import com.clemble.casino.server.game.construct.ServerGameConstructionService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class GameConstructionController<State extends GameState> implements GameConstructionService, ExternalController {

    final private GameConstructionRepository constructionRepository;
    final private ServerGameConstructionService constructionService;
    final private ServerGameConfigurationService configurationService;

    public GameConstructionController(
            final GameConstructionRepository constructionRepository,
            final ServerGameConstructionService matchingService,
            final ServerGameConfigurationService configurationService) {
        this.constructionService = checkNotNull(matchingService);
        this.configurationService = checkNotNull(configurationService);
        this.constructionRepository = checkNotNull(constructionRepository);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_SESSIONS, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameConstruction automatch(@RequestBody final PlayerGameConstructionRequest gameRequest) {
        // Step 1. Checking that provided specification was valid
        if (!configurationService.isValid(gameRequest.getConfiguration()))
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        // Step 2. Invoking actual matching service
        return constructionService.construct(gameRequest);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_CONSTRUCTION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameConstruction getConstruction(@PathVariable("game") final Game game, @PathVariable("session") final String session) {
        // Step 1. Searching for construction
        GameConstruction construction = constructionRepository.findOne(new GameSessionKey(game, session));
        // Step 2. Sending error in case resource not found
        if (construction == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GameConstructionDoesNotExistent);
        // Step 3. Returning construction
        return construction;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.GAME_CONSTRUCTION_RESPONSES_PLAYER, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerAwareEvent getResponce(@PathVariable("game") final Game game, @PathVariable("session") final String session, @PathVariable("playerId") final String player) {
        return constructionRepository.findOne(new GameSessionKey(game, session)).getResponses().fetchAction(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_CONSTRUCTION_RESPONSES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameConstruction reply(@PathVariable("game") final Game game, @PathVariable("session") String sessionId, @RequestBody final InvitationResponseEvent gameRequest) {
        // Step 1. Invoking actual matching service
        return constructionService.invitationResponsed(gameRequest);
    }

}
