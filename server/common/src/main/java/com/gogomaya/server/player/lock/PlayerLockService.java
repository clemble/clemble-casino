package com.gogomaya.server.player.lock;

import java.util.Collection;

public interface PlayerLockService {

    public void lock(long player);

    public void lock(Collection<Long> players);

    public void unlock(long player);

    public void unlock(Collection<Long> players);

}
