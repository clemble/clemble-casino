package com.clemble.casino.server.web.game.session;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.clemble.casino.game.GameState;
import com.clemble.casino.game.construct.AutomaticGameRequest;
import com.clemble.casino.game.construct.GameConstruction;
import com.clemble.casino.game.service.AutoGameConstructionService;
import com.clemble.casino.server.ExternalController;
import com.clemble.casino.server.game.construction.auto.ServerAutoGameConstructionService;
import com.clemble.casino.web.game.GameWebMapping;
import com.clemble.casino.web.mapping.WebMapping;

@Controller
public class AutoGameConstructionController<State extends GameState> implements AutoGameConstructionService, ExternalController {

    final private ServerAutoGameConstructionService autoConstructionService;

    public AutoGameConstructionController(final ServerAutoGameConstructionService autoConstructionService) {
        this.autoConstructionService = checkNotNull(autoConstructionService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = GameWebMapping.GAME_CONSTRUCTION_AUTO, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public @ResponseBody GameConstruction construct(@RequestBody final AutomaticGameRequest request) {
        // Step 1. Checking that provided specification was valid
        // TODO return (through valid annotation) 
        // if (!configurationService.isValid(gameRequest.getConfiguration()))
        //    throw ClembleCasinoException.fromError(ClembleCasinoError.GameSpecificationInvalid);
        // Step 2. Invoking actual matching service
        return autoConstructionService.construct(request);
    }

}
