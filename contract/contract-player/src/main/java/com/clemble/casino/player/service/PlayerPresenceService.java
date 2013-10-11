package com.clemble.casino.player.service;

import java.util.List;

import com.clemble.casino.player.PlayerPresence;

public interface PlayerPresenceService {

    public PlayerPresence getPresence(String player);

    public List<PlayerPresence> getPresences(List<String> players);

}
