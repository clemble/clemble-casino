package com.clemble.casino.server.web.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.player.PlayerSocialNetwork;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.web.mapping.WebMapping;
import com.clemble.casino.web.player.PlayerWebMapping;

@Controller
public class PlayerConnectionController implements PlayerConnectionService {

    final private PlayerSocialNetworkRepository connectionsRepository;

    public PlayerConnectionController(PlayerSocialNetworkRepository connectionsRepository) {
        this.connectionsRepository = connectionsRepository;
    }

    @Override
    @RequestMapping(value = PlayerWebMapping.CONNECTIONS_PLAYER, method = RequestMethod.GET, produces = WebMapping.PRODUCES)
    public @ResponseBody List<String> getConnections(String player) {
        // Step 1. Generating result collection
        List<String> playerCollection = new ArrayList<>();
        // Step 2. Going through existing connections
        Iterator<PlayerSocialNetwork> connections = connectionsRepository.findRelations(player).iterator();
        while (connections.hasNext()) {
            playerCollection.add(connections.next().getPlayer());
        }
        return playerCollection;
    }

}
