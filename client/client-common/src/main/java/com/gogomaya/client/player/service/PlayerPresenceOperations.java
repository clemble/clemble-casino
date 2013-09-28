package com.gogomaya.client.player.service;

import java.util.List;

import com.gogomaya.player.PlayerPresence;

public interface PlayerPresenceOperations {

    public PlayerPresence getPresence();

    public PlayerPresence getPresence(String player);

    public List<PlayerPresence> getPresences(List<String> players);

}
