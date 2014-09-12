package com.clemble.casino.server.game.construction.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.server.game.construction.repository.GameConstructionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.PlayerAwareEvent;
import com.clemble.casino.game.construction.AvailabilityGameRequest;
import com.clemble.casino.game.construction.GameConstruction;
import com.clemble.casino.game.construction.event.InvitationResponseEvent;
import com.clemble.casino.game.construction.service.AvailabilityGameConstructionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.construction.availability.ServerAvailabilityGameConstructionService;
import static com.clemble.casino.game.GameWebMapping.*;
import com.clemble.casino.web.mapping.WebMapping;

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

    @Override
    @RequestMapping(method = RequestMethod.POST, value = CONSTRUCTION_AVAILABILITY, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public GameConstruction construct(@RequestBody AvailabilityGameRequest gameRequest) {
        return availabilityConstructionService.construct(gameRequest);
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
    public PlayerAwareEvent getReply(@PathVariable("session") final String session, @PathVariable("playerId") final String player) {
        return constructionRepository.findOne(session).getResponses().filterAction(player);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = CONSTRUCTION_RESPONSES, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameConstruction reply(@RequestBody final InvitationResponseEvent gameRequest) {
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
