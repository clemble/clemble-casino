package com.gogomaya.client.player.service;

import java.util.List;

import com.gogomaya.player.PlayerPresence;
import com.gogomaya.player.service.PlayerPresenceService;

public class SimplePlayerPresenceOperations implements PlayerPresenceOperations {

    final private long playerId;
    final private PlayerPresenceService playerPresenceService;

    public SimplePlayerPresenceOperations(long playerId, PlayerPresenceService playerPresenceService) {
        this.playerId = playerId;
        this.playerPresenceService = playerPresenceService;
    }

    @Override
    public PlayerPresence getPresence() {
        return playerPresenceService.getPresence(playerId);
    }

    @Override
    public PlayerPresence getPresence(long player) {
        return playerPresenceService.getPresence(player);
    }

    @Override
    public List<PlayerPresence> getPresences(List<Long> players) {
        return playerPresenceService.getPresences(players);
    }
}
