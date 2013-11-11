package com.clemble.casino.android.player;


import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.web.player.PlayerWebMapping;
import com.clemble.casino.client.service.RestClientService;

public class AndroidPlayerPresenceService implements PlayerPresenceService {

    final private RestClientService restClientService;

    public AndroidPlayerPresenceService(RestClientService restClientService) {
        this.restClientService = checkNotNull(restClientService);
    }

    @Override
    public PlayerPresence getPresence(String player) {
        return restClientService.getForEntity(PlayerWebMapping.PLAYER_PRESENCE, PlayerPresence.class, player);
    }

    @Override
    public List<PlayerPresence> getPresences(List<String> players) {
        if(players == null || players.isEmpty())
            return new ArrayList<>();
        StringBuilder url = new StringBuilder(PlayerWebMapping.PLAYER_PRESENCES)
            .append("?")
            .append(PlayerWebMapping.PLAYER_PRESENCES_PARAM)
            .append("=");
        for(int i = 0; i < players.size(); i++) {
            if(i != 0)
                url.append(",");
            url.append(players.get(i));
        }
        return restClientService.getForEntityList(url, PlayerPresence.class);
    }
}
