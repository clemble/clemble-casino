package com.clemble.casino.client.player;

import java.util.List;

import com.clemble.casino.player.PlayerPresence;

public interface PlayerPresenceOperations {

    public PlayerPresence getPresence();

    public PlayerPresence getPresence(String player);

    public List<PlayerPresence> getPresences(List<String> players);

}
