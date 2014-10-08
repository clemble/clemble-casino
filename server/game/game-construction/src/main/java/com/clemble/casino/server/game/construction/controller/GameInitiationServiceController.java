package com.clemble.casino.server.game.construction.controller;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.server.game.construction.service.ServerGameInitiationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.game.lifecycle.initiation.GameInitiation;
import com.clemble.casino.game.lifecycle.initiation.service.GameInitiationService;
import com.clemble.casino.server.ExternalController;

import java.util.Collection;

import static com.clemble.casino.game.GameWebMapping.*;

@RestController
public class GameInitiationServiceController implements GameInitiationService, ExternalController {

    final private ServerGameInitiationService initiationService;

    public GameInitiationServiceController(ServerGameInitiationService initiationService) {
        this.initiationService = checkNotNull(initiationService);
    }

    @Override
    public GameInitiation confirm(String sessionKey) {
        throw new IllegalAccessError();
    }

    @Override
    public Collection<GameInitiation> getPending() {
        throw new IllegalArgumentException();
    }

    @RequestMapping(method = RequestMethod.GET, value = INITIATION_PENDING, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<GameInitiation> getPending(@CookieValue("player") String player) {
        return initiationService.getPending(player);
    }


    @RequestMapping(method = RequestMethod.POST, value = INITIATION_READY, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.CREATED)
    public GameInitiation confirm(@CookieValue("player") String player, @PathVariable("session") final String session) {
        return initiationService.confirm(player, session);
    }

}
