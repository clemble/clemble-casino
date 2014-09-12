package com.clemble.casino.server.game.controller;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.server.game.construction.ServerGameInitiationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.construction.GameInitiation;
import com.clemble.casino.game.construction.service.GameInitiationService;
import com.clemble.casino.server.ExternalController;

import javax.ws.rs.CookieParam;

import static com.clemble.casino.game.GameWebMapping.*;

@RestController
public class GameInitiationController implements GameInitiationService, ExternalController {

    final private ServerGameInitiationService initiationService;

    public GameInitiationController(ServerGameInitiationService initiationService) {
        this.initiationService = checkNotNull(initiationService);
    }

    @Override
    public GameInitiation confirm(String sessionKey) {
        throw new IllegalAccessError();
    }

    @RequestMapping(method = RequestMethod.POST, value = INITIATION_READY, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameInitiation confirm(@CookieValue("player") String player, @PathVariable("session") final String session) {
        return initiationService.confirm(session, player);
    }

}
