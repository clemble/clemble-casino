package com.clemble.casino.client.event;

import com.clemble.casino.event.Event;
import com.clemble.casino.player.PlayerPresence;

public class PresenceEventSelector implements EventSelector {

    final private String player;

    public PresenceEventSelector(String player) {
        this.player = player;
    }

    @Override
    public boolean filter(Event event) {
        return (event instanceof PlayerPresence && player.equals(((PlayerPresence) event).getPlayer())) ? true : false;
    }

}
