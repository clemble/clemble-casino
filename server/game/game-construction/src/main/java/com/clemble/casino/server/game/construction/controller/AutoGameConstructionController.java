package com.clemble.casino.server.game.construction.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.construct.AutoGameConstructionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.construction.auto.ServerAutoGameConstructionService;
import com.clemble.casino.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

import java.util.Collection;

@RestController
public class AutoGameConstructionController implements AutoGameConstructionService, ExternalController {

    final private ServerAutoGameConstructionService autoConstructionService;

    public AutoGameConstructionController(final ServerAutoGameConstructionService autoConstructionService) {
        this.autoConstructionService = checkNotNull(autoConstructionService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.CONSTRUCTION_AUTO, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameConstruction construct(@RequestBody final AutomaticGameRequest request) {
        // Step 1. Checking that provided specification was valid
        // TODO return (through valid annotation) 
        // if (!configurationService.isValid(gameRequest.getConfiguration()))
        //    throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        // Step 2. Invoking actual matching service
        return autoConstructionService.construct(request);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = GameWebMapping.CONSTRUCTION_AUTO_PENDING, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GameConstruction> getPending(@PathVariable("player") String player) {
        return autoConstructionService.getPending(player);
    }

}
