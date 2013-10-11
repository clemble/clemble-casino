package com.clemble.casino.server.player.lock;

import java.util.Collection;

public interface PlayerLockService {

    public void lock(String player);

    public void lock(Collection<String> players);

    public void unlock(String player);

    public void unlock(Collection<String> players);

}
