package com.clemble.casino.server.web.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.stereotype.Controller;

import com.clemble.casino.player.event.PlayerConnection;
import com.clemble.casino.player.event.PlayerConnectionStatus;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.player.PlayerSocialNetwork;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;

@Controller
public class PlayerConnectionController implements PlayerConnectionService {

    final private PlayerSocialNetworkRepository connectionsRepository;

    public PlayerConnectionController(PlayerSocialNetworkRepository connectionsRepository) {
        this.connectionsRepository = connectionsRepository;
    }

    @Override
    public Collection<PlayerConnection> getConnections(String player) {
        Iterator<PlayerSocialNetwork> connections = connectionsRepository.findRelations(player).iterator();
        Collection<PlayerConnection> playerCollection = new ArrayList<>();
        while (connections.hasNext()) {
            playerCollection.add(new PlayerConnection(connections.next().getPlayer(), PlayerConnectionStatus.connected));
        }
        return playerCollection;
    }

}
