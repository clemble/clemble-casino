package com.clemble.casino.android.player;


import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.AbstractClembleCasinoOperations;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.web.player.PlayerWebMapping;

public class AndroidPlayerPresenceService extends AbstractClembleCasinoOperations implements PlayerPresenceService {

    final private RestTemplate restTemplate;

    public AndroidPlayerPresenceService(RestTemplate restClientService, ServerRegistry serverRegistry) {
        super(serverRegistry);
        this.restTemplate = checkNotNull(restClientService);
    }

    @Override
    public PlayerPresence getPresence(String player) {
        URI presenceURI = buildUriWith(PlayerWebMapping.PLAYER_PRESENCE, player);
        // Step 1. Singleton GET request
        return restTemplate
                .getForEntity(presenceURI, PlayerPresence.class)
                .getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PlayerPresence> getPresences(List<String> players) {
        // Step 1. Sanity check
        if(players == null || players.isEmpty())
            return new ArrayList<PlayerPresence>();
       // Step 2. Generating multivalue query map
       MultiValueMap<String, String> query = new LinkedMultiValueMap<String, String>();
       for(String player: players)
           query.set(PlayerWebMapping.PLAYER_PRESENCES_PARAM, player);
       // Step 3. Requesting through RestTemplate
       return restTemplate
           .getForEntity(buildUri(PlayerWebMapping.PLAYER_PRESENCES, query), List.class)
           .getBody();
    }
}
