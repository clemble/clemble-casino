package com.clemble.casino.server.player.lock;

import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class JavaPlayerLockService implements PlayerLockService {

    final private LoadingCache<String, PlayerGroupLock> LOCKS = CacheBuilder.newBuilder().build(new CacheLoader<String, PlayerGroupLock>() {

        @Override
        public PlayerGroupLock load(String playerGroup) throws Exception {
            return new PlayerGroupLock(playerGroup);
        }

    });

    public static class PlayerGroupLock implements Comparable<PlayerGroupLock> {
        final public ReentrantLock lock = new ReentrantLock();
        final public String playerGroup;

        public PlayerGroupLock(String playerGroup) {
            this.playerGroup = playerGroup;
        }

        @Override
        public int compareTo(PlayerGroupLock o) {
            return playerGroup.compareTo(o.playerGroup);
        }
    }

    @Override
    public void lock(String player) {
        PlayerGroupLock playerGroupLock = getLock(player);
        if (!playerGroupLock.lock.tryLock()) {
            try {
                if (!playerGroupLock.lock.tryLock(500, TimeUnit.NANOSECONDS)) {
                    playerGroupLock.lock.lock();
                }
            } catch (InterruptedException e) {
                throw ClembleCasinoException.fromError(ClembleCasinoError.PlayerLockAcquireFailure);
            }
        }
    }

    @Override
    public void lock(Collection<String> players) {
        TreeSet<PlayerGroupLock> playerGroupLocks = getLocks(players);
        TreeSet<PlayerGroupLock> acquiredLocks = new TreeSet<PlayerGroupLock>();
        do {
            if (!acquiredLocks.isEmpty()) {
                unlock(acquiredLocks);
                acquiredLocks.clear();
            }
            for (PlayerGroupLock groupLock : playerGroupLocks) {
                if (groupLock.lock.tryLock()) {
                    acquiredLocks.add(groupLock);
                } else {
                    break;
                }
            }
        } while (acquiredLocks.size() != playerGroupLocks.size());

    }

    @Override
    public void unlock(Collection<String> players) {
        unlock(getLocks(players));
    }

    private void unlock(TreeSet<PlayerGroupLock> groupLocks) {
        for (PlayerGroupLock acquiredLock : groupLocks)
            acquiredLock.lock.unlock();
    }

    @Override
    public void unlock(String player) {
        PlayerGroupLock playerGroupLock = getLock(player);
        playerGroupLock.lock.unlock();
    }

    private TreeSet<PlayerGroupLock> getLocks(Collection<String> players) {
        TreeSet<PlayerGroupLock> locks = new TreeSet<PlayerGroupLock>();
        for (String player : players)
            locks.add(getLock(player));
        return locks;
    }

    private PlayerGroupLock getLock(String player) {
        return LOCKS.getUnchecked(player);
    }

}
