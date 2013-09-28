package com.gogomaya.server.player.presence;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.gogomaya.game.GameSessionKey;
import com.gogomaya.player.PlayerPresence;
import com.gogomaya.player.Presence;
import com.gogomaya.server.player.notification.PlayerNotificationListener;

public interface PlayerPresenceServerService {

    public boolean isAvailable(String player);

    public boolean areAvailable(Collection<String> players);

    public PlayerPresence getPresence(String player);

    public List<PlayerPresence> getPresences(Collection<String> presences);

    public Date markOnline(String player);

    public void markOffline(String player);

    public boolean markPlaying(String player, GameSessionKey session);

    public boolean markPlaying(Collection<String> players, GameSessionKey session);

    public void subscribe(String playerId, PlayerNotificationListener<Presence> messageListener);

    public void subscribe(Collection<String> players, PlayerNotificationListener<Presence> messageListener);

    public void unsubscribe(String player, PlayerNotificationListener<Presence> messageListener);

    public void unsubscribe(Collection<String> players, PlayerNotificationListener<Presence> playerStateListener);

}
