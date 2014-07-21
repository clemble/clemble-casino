package com.clemble.casino.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

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
import com.clemble.casino.game.construct.AvailabilityGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.schedule.InvitationResponseEvent;
import com.clemble.casino.game.service.AvailabilityGameConstructionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.construction.availability.ServerAvailabilityGameConstructionService;
import com.clemble.casino.server.repository.game.GameConstructionRepository;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class AvailabilityGameConstructionController implements AvailabilityGameConstructionService, ExternalController {

    final private GameConstructionRepository constructionRepository;
    final private ServerAvailabilityGameConstructionService availabilityConstructionService;

    public AvailabilityGameConstructionController(
            final ServerAvailabilityGameConstructionService availabilityConstructionService,
            final GameConstructionRepository constructionRepository) {
        this.constructionRepository = checkNotNull(constructionRepository);
        this.availabilityConstructionService = checkNotNull(availabilityConstructionService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.CONSTRUCTION_AVAILABILITY, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody GameConstruction construct(@RequestBody AvailabilityGameRequest gameRequest) {
        return availabilityConstructionService.construct(gameRequest);
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
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.CONSTRUCTION_RESPONSES_PLAYER, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody PlayerAwareEvent getReply(@PathVariable("game") final Game game, @PathVariable("session") final String session, @PathVariable("playerId") final String player) {
        return constructionRepository.findOne(new GameSessionKey(game, session)).getResponses().filterAction(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.CONSTRUCTION_RESPONSES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameConstruction reply(@RequestBody final InvitationResponseEvent gameRequest) {
        // Step 1. Invoking actual matching service
        return availabilityConstructionService.invitationResponded(gameRequest);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.CONSTRUCTION_AVAILABILITY_PENDING, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public @ResponseBody Collection<GameInitiation> getPending(@PathVariable("playerId") String player) {
        return availabilityConstructionService.getPending(player);
    }

}
