package com.clemble.casino.server.game.controller;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.server.game.construction.ServerGameInitiationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.service.GameInitiationService;
import com.clemble.casino.server.ExternalController;
import static com.clemble.casino.game.GameWebMapping.*;

@RestController
public class GameInitiationController implements GameInitiationService, ExternalController {

    final private ServerGameInitiationService initiationService;

    public GameInitiationController(ServerGameInitiationService initiationService) {
        this.initiationService = checkNotNull(initiationService);
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = INITIATION_READY, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameInitiation confirm(@PathVariable("session") final String session, @PathVariable("playerId") final String player) {
        return initiationService.confirm(session, player);
    }

}
