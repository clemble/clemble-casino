package com.clemble.casino.server.connection.controller;

import java.util.Set;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.connection.service.ServerPlayerConnectionService;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.web.mapping.WebMapping;
import static com.clemble.casino.web.player.PlayerWebMapping.*;

@RestController
public class PlayerConnectionServiceController implements PlayerConnectionService {

    final private ServerPlayerConnectionService connectionService;

    public PlayerConnectionServiceController(ServerPlayerConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public PlayerConnections myConnections() {
        throw new IllegalAccessError();
    }

    @RequestMapping(value = MY_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerConnections myConnections(@CookieValue("player") String player) {
        return connectionService.myConnections(player);
    }

    @Override
    public Set<ConnectionKey> myOwnedConnections(){
        throw new IllegalAccessError();
    }


    @RequestMapping(value = MY_OWNED_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Set<ConnectionKey> myOwnedConnections(String me) {
        return connectionService.myOwnedConnections(me);
    }

    @Override
    public Set<ConnectionKey> myConnectedConnections() {
        throw new IllegalAccessError();
    }

    @RequestMapping(value = MY_CONNECTED_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Set<ConnectionKey> myConnectedConnections(String me) {
        return connectionService.myConnectedConnections(me);
    }

    @Override
    @RequestMapping(value = PLAYER_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public PlayerConnections getConnections(@PathVariable("player") String player) {
        return connectionService.getConnections(player);
    }

    @Override
    @RequestMapping(value = PLAYER_OWNED_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Set<ConnectionKey> getOwnedConnections(String player) {
        return connectionService.getOwnedConnections(player);
    }

    @Override
    @RequestMapping(value = PLAYER_CONNECTION_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    @ResponseStatus(value = HttpStatus.OK)
    public Set<ConnectionKey> getConnectedConnection(String player) {
        return connectionService.getConnectedConnection(player);
    }

}
