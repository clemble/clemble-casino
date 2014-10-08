package com.clemble.casino.server.game.construction.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.game.lifecycle.construction.event.PlayerInvitationAction;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.player.event.PlayerEvent;
import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.game.lifecycle.construction.AvailabilityGameRequest;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.game.lifecycle.construction.service.AvailabilityGameConstructionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.construction.service.ServerAvailabilityGameConstructionService;
import static com.clemble.casino.game.GameWebMapping.*;
import com.clemble.casino.WebMapping;

@RestController
public class AvailabilityGameConstructionController implements AvailabilityGameConstructionService, ExternalController {

    final private GameConstructionRepository constructionRepository;
    final private ServerAvailabilityGameConstructionService availabilityConstructionService;

    public AvailabilityGameConstructionController(
            final ServerAvailabilityGameConstructionService availabilityConstructionService,
            final GameConstructionRepository constructionRepository) {
        this.constructionRepository = checkNotNull(constructionRepository);
        this.availabilityConstructionService = checkNotNull(availabilityConstructionService);
    }

    public GameConstruction construct(AvailabilityGameRequest gameRequest) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = CONSTRUCTION_AVAILABILITY, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameConstruction construct(@CookieValue("player") String player, @RequestBody AvailabilityGameRequest gameRequest) {
        return availabilityConstructionService.construct(player, gameRequest);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = CONSTRUCTION, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameConstruction getConstruction(@PathVariable("session") final String session) {
        // Step 1. Searching for construction
        GameConstruction construction = constructionRepository.findOne(session);
        // Step 2. Sending error in case resource not found
        if (construction == null)
            throw ClembleCasinoException.withKey(ClembleCasinoError.GameConstructionDoesNotExistent, session);
        // Step 3. Returning construction
        return construction;
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = CONSTRUCTION_RESPONSES_PLAYER, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerAction getReply(@PathVariable("session") final String session, @PathVariable("playerId") final String player) {
        return constructionRepository.findOne(session).getResponses().filterAction(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = CONSTRUCTION_RESPONSES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameConstruction reply(@RequestBody final PlayerInvitationAction gameRequest) {
        // Step 1. Invoking actual matching service
        return availabilityConstructionService.invitationResponded(gameRequest);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = CONSTRUCTION_AVAILABILITY_PENDING, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GameConstruction> getPending(@PathVariable("playerId") String player) {
        return availabilityConstructionService.getPending(player);
    }

}
