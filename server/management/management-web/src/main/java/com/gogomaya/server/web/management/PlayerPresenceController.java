package com.gogomaya.server.web.management;

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
import com.gogomaya.web.management.ManagementWebMapping;
import com.gogomaya.web.mapping.WebMapping;

@Controller
public class PlayerPresenceController implements PlayerPresenceService {

    final private PlayerPresenceServerService presenceServerService;

    public PlayerPresenceController(PlayerPresenceServerService playerPresenceServerService) {
        this.presenceServerService = checkNotNull(playerPresenceServerService);
    }

    @Override
    @RequestMapping(params = ManagementWebMapping.MANAGEMENT_PLAYER_PRESENCE, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody PlayerPresence getPresence(@PathVariable("playerId") long player) {
        return presenceServerService.getPresence(player);
    }

    @Override
    @RequestMapping(params = ManagementWebMapping.MANAGEMENT_PLAYER_PRESENCES, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody List<PlayerPresence> getPresences(@RequestParam(required = true, value = "players") List<Long> players) {
        return presenceServerService.getPresences(players);
    }

}
