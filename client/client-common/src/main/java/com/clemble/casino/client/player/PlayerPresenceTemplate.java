package com.clemble.casino.client.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.List;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.event.NotificationMapping;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.service.PlayerPresenceService;

public class PlayerPresenceTemplate implements PlayerPresenceOperations {

    final private String player;
    final private PlayerPresenceService playerPresenceService;
    final private EventListenerOperations listenerOperations;

    public PlayerPresenceTemplate(String player, PlayerPresenceService playerPresenceService, EventListenerOperations listenerOperations) {
        this.player = checkNotNull(player);
        this.playerPresenceService = checkNotNull(playerPresenceService);
        this.listenerOperations = checkNotNull(listenerOperations);
    }

    @Override
    public PlayerPresence getPresence() {
        return playerPresenceService.getPresence(player);
    }

    @Override
    public PlayerPresence getPresence(String player) {
        return playerPresenceService.getPresence(player);
    }

    @Override
    public List<PlayerPresence> getPresences(String... players) {
        return getPresences(Arrays.asList(players));
    }

    @Override
    public List<PlayerPresence> getPresences(List<String> players) {
        return playerPresenceService.getPresences(players);
    }

    @Override
    public void subscribe(String player, EventListener listener) {
        if(player == null || listener == null)
            throw new IllegalArgumentException();
        listenerOperations.subscribe(player + NotificationMapping.PLAYER_PRESENCE_NOTIFICATION, listener);
    }

    @Override
    public void subscribe(List<String> players, EventListener listener) {
        for(String player: players)
            subscribe(player, listener);
    }

}
