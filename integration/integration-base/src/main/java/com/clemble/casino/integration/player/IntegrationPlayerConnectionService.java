package com.clemble.casino.integration.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Set;

import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;

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
    public Set<String> myConnections() {
        return connectionService.myConnections(player);
    }

    @Override
    public Set<String> getConnections(String player) {
        return connectionService.getConnections(player);
    }

}
