package com.clemble.casino.server.player.presence;

import java.util.Collection;

import com.clemble.casino.player.Presence;
import com.clemble.casino.server.player.notification.PlayerNotificationListener;

public interface PlayerPresenceListenerService {

    public void subscribe(String playerId, PlayerNotificationListener<Presence> messageListener);

    public void subscribe(Collection<String> players, PlayerNotificationListener<Presence> messageListener);

    public void unsubscribe(String player, PlayerNotificationListener<Presence> messageListener);

    public void unsubscribe(Collection<String> players, PlayerNotificationListener<Presence> playerStateListener);

}
