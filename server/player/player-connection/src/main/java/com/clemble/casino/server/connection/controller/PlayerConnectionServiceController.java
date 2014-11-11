package com.clemble.casino.server.connection.controller;

import java.util.Set;

import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.connection.service.PlayerGraphService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.WebMapping;
import static com.clemble.casino.player.PlayerConnectionWebMapping.*;

@RestController
public class PlayerConnectionServiceController implements PlayerConnectionService {

    final private PlayerGraphService connectionService;

    public PlayerConnectionServiceController(PlayerGraphService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public Set<String> myConnections() {
        throw new IllegalAccessError();
    }

    @RequestMapping(value = MY_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Set<String> myConnections(@CookieValue("player") String player) {
        return connectionService.getConnections(player);
    }

    @Override
    @RequestMapping(value = PLAYER_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Set<String> getConnections(@PathVariable("player") String player) {
        return connectionService.getConnections(player);
    }

}
