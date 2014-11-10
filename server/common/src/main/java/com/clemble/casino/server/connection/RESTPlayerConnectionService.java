package com.clemble.casino.server.connection;

import com.clemble.casino.player.PlayerConnections;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.utils.CollectionUtils;
import com.google.common.base.Preconditions;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Set;

import static com.clemble.casino.player.PlayerWebMapping.*;

/**
 * Created by mavarazy on 11/9/14.
 */
public class RESTPlayerConnectionService implements PlayerConnectionService {

    final private String host;
    final private RestTemplate restTemplate;

    public RESTPlayerConnectionService(String host, RestTemplate restTemplate) {
        this.host = Preconditions.checkNotNull(host);
        this.restTemplate = Preconditions.checkNotNull(restTemplate);
    }

    @Override
    public PlayerConnections getConnections(String player) {
        // Step 1. Fetching player connections
        String playerUri = toConnectionUrl(PLAYER_CONNECTIONS).replace("{player}", player).replace("{host}", host);
        // Step 3. Requesting through RestTemplate
        return restTemplate.getForObject(playerUri, PlayerConnections.class);

    }

    @Override
    public Set<ConnectionKey> getOwnedConnections(String player) {
        // Step 1. Fetching player connections
        String playerUri = toConnectionUrl(PLAYER_OWNED_CONNECTIONS).replace("{player}", player).replace("{host}", host);
        // Step 3. Requesting through RestTemplate
        return CollectionUtils.immutableSet(restTemplate.getForObject(playerUri, ConnectionKey[].class));
    }

    @Override
    public Set<String> getConnectedConnection(String player) {
        // Step 1. Fetching player connections
        String playerUri = toConnectionUrl(PLAYER_CONNECTION_CONNECTIONS).replace("{player}", player).replace("{host}", host);
        // Step 3. Requesting through RestTemplate
        return CollectionUtils.immutableSet(restTemplate.getForObject(playerUri, String[].class));
    }

    @Override
    public PlayerConnections myConnections() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConnectionKey> myOwnedConnections() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> myConnectedConnections() {
        throw new UnsupportedOperationException();
    }
}
