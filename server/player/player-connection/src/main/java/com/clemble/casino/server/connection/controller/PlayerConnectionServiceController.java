package com.clemble.casino.server.connection.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.clemble.casino.player.service.PlayerConnectionServiceContract;
import com.clemble.casino.player.social.ClembleSocialUtils;
import com.clemble.casino.server.connection.PlayerConnectionKey;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.clemble.casino.server.connection.PlayerConnectionNetwork;
import com.clemble.casino.server.connection.repository.PlayerConnectionNetworkRepository;
import com.clemble.casino.web.mapping.WebMapping;
import static com.clemble.casino.web.player.PlayerWebMapping.*;

@Controller
public class PlayerConnectionServiceController implements PlayerConnectionServiceContract {

    final private PlayerConnectionNetworkRepository connectionsRepository;

    public PlayerConnectionServiceController(PlayerConnectionNetworkRepository connectionsRepository) {
        this.connectionsRepository = connectionsRepository;
    }

    @RequestMapping(value = MY_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody List<ConnectionKey> myConnections(@CookieValue("player") String player) {
        return getConnections(player);
    }

    @Override
    @RequestMapping(value = PLAYER_CONNECTIONS, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody List<ConnectionKey> getConnections(@PathVariable("player") String player) {
        // Step 1. Generating result collection
        List<ConnectionKey> connectionKeys = new ArrayList<>();
        PlayerConnectionNetwork playerConnections = connectionsRepository.findByPlayer(player);;
        for(PlayerConnectionKey connection: playerConnections.getConnections())
            connectionKeys.add(ClembleSocialUtils.fromString(connection.getConnectionKey()));
        // Step 2. Going through existing connections
        Iterator<PlayerConnectionNetwork> relatedConnections = connectionsRepository.findRelations(player).iterator();
        while (relatedConnections.hasNext()) {
            PlayerConnectionNetwork relatedConnection = relatedConnections.next();
            // Step 2.1. Removing owned connections
            for(PlayerConnectionKey connection: relatedConnection.getOwns())
                connectionKeys.remove(ClembleSocialUtils.fromString(connection.getConnectionKey()));
            // Step 2.2. Adding internal connection
            connectionKeys.add(new ConnectionKey(WebMapping.PROVIDER_ID, relatedConnection.getPlayer()));
        }
        // Step 3. Checking values
        return connectionKeys;
    }

}
