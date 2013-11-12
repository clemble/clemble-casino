package com.clemble.casino.android.player;


import static com.clemble.casino.utils.Preconditions.checkNotNull;

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
        return restTemplate
            .getForEntity(buildUriById(player, PlayerWebMapping.PLAYER_PRESENCE, "playerId", player), PlayerPresence.class)
            .getBody();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PlayerPresence> getPresences(List<String> players) {
        if(players == null || players.isEmpty())
            return new ArrayList<>();
       MultiValueMap<String, String> query = new LinkedMultiValueMap<>();
       for(String player: players)
           query.set(PlayerWebMapping.PLAYER_PRESENCES_PARAM, player);
       return restTemplate
           .getForEntity(buildUri(PlayerWebMapping.PLAYER_PRESENCES, query), List.class)
           .getBody();
    }
}
