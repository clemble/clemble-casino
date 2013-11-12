package com.clemble.casino.android.player;

import java.util.List;

import com.clemble.casino.client.player.PlayerPresenceOperations;
import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.service.PlayerPresenceService;

public class PlayerPresenceTemplate implements PlayerPresenceOperations {

    final private String player;
    final private PlayerPresenceService playerPresenceService;

    public PlayerPresenceTemplate(String player, PlayerPresenceService playerPresenceService) {
        this.player = player;
        this.playerPresenceService = playerPresenceService;
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
    public List<PlayerPresence> getPresences(List<String> players) {
        return playerPresenceService.getPresences(players);
    }
}
