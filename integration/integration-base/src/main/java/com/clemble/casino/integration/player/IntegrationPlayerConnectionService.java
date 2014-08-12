package com.clemble.casino.integration.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;
import java.util.Set;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import org.springframework.social.connect.ConnectionKey;

public class IntegrationPlayerConnectionService implements PlayerConnectionService {

    /**
     * Generated 30/12/13
     */
    private static final long serialVersionUID = 4966541707719576636L;

    final private String player;
    final private PlayerConnectionServiceController connectionService;

    public IntegrationPlayerConnectionService(String player, PlayerConnectionServiceController connectionService) {
        this.player = checkNotNull(player);
        this.connectionService = checkNotNull(connectionService);
    }

    @Override
    public PlayerConnections myConnections() {
        return connectionService.myConnections(player);
    }

    @Override
    public Set<ConnectionKey> myOwnedConnections() {
        return connectionService.myOwnedConnections(player);
    }

    @Override
    public Set<ConnectionKey> myConnectedConnections() {
        return connectionService.myConnectedConnections(player);
    }

    @Override
    public PlayerConnections getConnections(String player) {
        return connectionService.getConnections(player);
    }

    @Override
    public Set<ConnectionKey> getOwnedConnections(String player) {
        return connectionService.getOwnedConnections(player);
    }

    @Override
    public Set<ConnectionKey> getConnectedConnection(String player) {
        return connectionService.getConnectedConnection(player);
    }

}
