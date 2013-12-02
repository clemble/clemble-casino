package com.clemble.casino.server.player.presence;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.player.PlayerPresence;

public interface PlayerPresenceServerService {

    public boolean isAvailable(String player);

    public boolean areAvailable(Collection<String> players);

    public PlayerPresence getPresence(String player);

    public List<PlayerPresence> getPresences(Collection<String> presences);

    public Date markOnline(String player);

    public void markOffline(String player);

    public boolean markPlaying(String player, GameSessionKey session);

    public boolean markPlaying(Collection<String> players, GameSessionKey session);

}
