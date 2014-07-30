package com.clemble.casino.server.web.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.clemble.casino.player.event.PlayerConnection;
import com.clemble.casino.player.social.ClembleSocialUtils;
import com.clemble.casino.server.player.social.PlayerConnectionKey;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.player.social.PlayerSocialNetwork;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.web.mapping.WebMapping;
import static com.clemble.casino.web.player.PlayerWebMapping.*;

@Controller
public class PlayerConnectionController implements PlayerConnectionService {

    final private PlayerSocialNetworkRepository connectionsRepository;

    public PlayerConnectionController(PlayerSocialNetworkRepository connectionsRepository) {
        this.connectionsRepository = connectionsRepository;
    }

    @Override
    @RequestMapping(value = CONNECTION_PLAYER, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody List<ConnectionKey> getConnections(String player) {
        // Step 1. Generating result collection
        List<ConnectionKey> connectionKeys = new ArrayList<>();
        PlayerSocialNetwork playerConnections = connectionsRepository.findByPlayer(player);
        for(PlayerConnectionKey connection: playerConnections.getConnections())
            connectionKeys.add(ClembleSocialUtils.fromString(connection.getConnectionKey()));
        // Step 2. Going through existing connections
        Iterator<PlayerSocialNetwork> relatedConnections = connectionsRepository.findRelations(player).iterator();
        while (relatedConnections.hasNext()) {
            PlayerSocialNetwork relatedConnection = relatedConnections.next();
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
