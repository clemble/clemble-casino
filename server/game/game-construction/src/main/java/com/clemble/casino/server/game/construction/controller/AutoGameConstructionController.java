package com.clemble.casino.server.game.construction.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.lifecycle.construction.AutomaticGameRequest;
import com.clemble.casino.game.lifecycle.construction.GameConstruction;
import com.clemble.casino.game.lifecycle.construction.service.AutoGameConstructionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.construction.service.ServerAutoGameConstructionService;
import com.clemble.casino.game.GameWebMapping;
import com.clemble.casino.WebMapping;

import java.util.Collection;

@RestController
public class AutoGameConstructionController implements AutoGameConstructionService, ExternalController {

    final private ServerAutoGameConstructionService autoConstructionService;

    public AutoGameConstructionController(final ServerAutoGameConstructionService autoConstructionService) {
        this.autoConstructionService = checkNotNull(autoConstructionService);
    }

    @Override
    public GameConstruction construct(AutomaticGameRequest request) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.CONSTRUCTION_AUTO, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameConstruction construct(@CookieValue("player") String player, @RequestBody final AutomaticGameRequest request) {
        // Step 1. Checking that provided specification was valid
        // TODO return (through valid annotation) 
        // if (!configurationService.isValid(gameRequest.getConfiguration()))
        //    throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        // Step 2. Invoking actual matching service
        return autoConstructionService.construct(player, request);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.CONSTRUCTION_AUTO_PENDING, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GameConstruction> getPending(@PathVariable("player") String player) {
        return autoConstructionService.getPending(player);
    }

}
