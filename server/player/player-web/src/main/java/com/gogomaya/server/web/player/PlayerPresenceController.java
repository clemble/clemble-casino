package com.gogomaya.server.web.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gogomaya.player.PlayerPresence;
import com.gogomaya.player.service.PlayerPresenceService;
import com.gogomaya.server.player.presence.PlayerPresenceServerService;
import com.gogomaya.web.mapping.WebMapping;
import com.gogomaya.web.player.PlayerWebMapping;

@Controller
public class PlayerPresenceController implements PlayerPresenceService {

    final private PlayerPresenceServerService presenceServerService;

    public PlayerPresenceController(PlayerPresenceServerService playerPresenceServerService) {
        this.presenceServerService = checkNotNull(playerPresenceServerService);
    }

    @Override
    @RequestMapping(params = PlayerWebMapping.PLAYER_PRESENCE, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody PlayerPresence getPresence(@PathVariable("playerId") String player) {
        return presenceServerService.getPresence(player);
    }

    @Override
    @RequestMapping(params = PlayerWebMapping.PLAYER_PRESENCES, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody List<PlayerPresence> getPresences(@RequestParam(required = true, value = PlayerWebMapping.PLAYER_PRESENCES_PARAM) List<String> players) {
        return presenceServerService.getPresences(players);
    }

}
