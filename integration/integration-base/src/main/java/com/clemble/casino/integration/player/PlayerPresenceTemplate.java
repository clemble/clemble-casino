package com.clemble.casino.integration.player;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerController;
import com.clemble.casino.client.event.EventListenerControllerAgregate;
import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.event.NotificationMapping;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.PlayerPresenceChangedEvent;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerPresenceServiceContract;
import com.clemble.casino.server.presence.controller.player.PlayerPresenceServiceController;

public class PlayerPresenceTemplate implements PlayerPresenceService {

    final private String player;
    final private PlayerPresenceServiceController playerPresenceService;

    public PlayerPresenceTemplate(String player, PlayerPresenceServiceController playerPresenceService) {
        this.player = checkNotNull(player);
        this.playerPresenceService = checkNotNull(playerPresenceService);
    }

    @Override
    public PlayerPresence myPresence() {
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

}
