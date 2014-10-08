package com.clemble.casino.server.presence.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.clemble.casino.server.ExternalController;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.service.PlayerPresenceServiceContract;
import com.clemble.casino.server.player.presence.ServerPlayerPresenceService;

import static com.clemble.casino.player.PresenceWebMapping.*;

@RestController
public class PlayerPresenceServiceController implements PlayerPresenceServiceContract, ExternalController {

    final private ServerPlayerPresenceService presenceServerService;

    public PlayerPresenceServiceController(ServerPlayerPresenceService playerPresenceServerService) {
        this.presenceServerService = checkNotNull(playerPresenceServerService);
    }

    @RequestMapping(value = MY_PRESENCE, method = RequestMethod.GET, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerPresence myPresence(@CookieValue("player") String player) {
        return presenceServerService.getPresence(player);
    }

    @Override
    @RequestMapping(value = PLAYER_PRESENCE, method = RequestMethod.GET, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerPresence getPresence(@PathVariable("player") String player) {
        return presenceServerService.getPresence(player);
    }

    @Override
    @RequestMapping(value = PLAYER_PRESENCES, method = RequestMethod.GET, produces = PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public List<PlayerPresence> getPresences(@RequestParam(required = true, value = PLAYER_PRESENCES_PARAM) List<String> players) {
        return presenceServerService.getPresences(players);
    }

}
