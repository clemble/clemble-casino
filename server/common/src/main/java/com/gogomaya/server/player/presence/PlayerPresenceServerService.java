package com.gogomaya.server.player.presence;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.gogomaya.player.PlayerPresence;
import com.gogomaya.player.Presence;
import com.gogomaya.server.player.notification.PlayerNotificationListener;

public interface PlayerPresenceServerService {

    public boolean isAvailable(long player);

    public boolean areAvailable(Collection<Long> players);

    public PlayerPresence getPresence(long player);

    public List<PlayerPresence> getPresences(Collection<Long> presences);

    public Date markOnline(long player);

    public void markOffline(long player);

    public boolean markPlaying(long player, long session);

    public boolean markPlaying(Collection<Long> players, long session);

    public void subscribe(long playerId, PlayerNotificationListener<Presence> messageListener);

    public void subscribe(Collection<Long> players, PlayerNotificationListener<Presence> messageListener);

    public void unsubscribe(long player, PlayerNotificationListener<Presence> messageListener);

    public void unsubscribe(Collection<Long> players, PlayerNotificationListener<Presence> playerStateListener);

}
