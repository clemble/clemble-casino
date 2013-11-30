package com.clemble.casino.client.player;

import java.util.List;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.player.PlayerPresence;

public interface PlayerPresenceOperations {

    public PlayerPresence getPresence();

    public PlayerPresence getPresence(String player);

    public List<PlayerPresence> getPresences(String ... players);

    public List<PlayerPresence> getPresences(List<String> players);

    public void subscribe(String player, EventListener listener);

    public void subscribe(List<String> player, EventListener listener);

}
