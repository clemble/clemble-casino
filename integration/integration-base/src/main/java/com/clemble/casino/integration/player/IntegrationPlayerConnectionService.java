package com.clemble.casino.integration.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.List;

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
    public List<ConnectionKey> myConnections() {
        return connectionService.myConnections(player);
    }

    @Override
    public List<ConnectionKey> getConnections(String player) {
        return connectionService.getConnections(player);
    }

}
