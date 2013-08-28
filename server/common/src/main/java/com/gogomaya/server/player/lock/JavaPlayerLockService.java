package com.gogomaya.server.player.lock;

import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.gogomaya.error.GogomayaError;
import com.gogomaya.error.GogomayaException;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class JavaPlayerLockService implements PlayerLockService {

    final private static int LOCKS_PER_USER = 10;

    final private LoadingCache<Long, PlayerGroupLock> LOCKS = CacheBuilder.newBuilder().build(new CacheLoader<Long, PlayerGroupLock>() {

        @Override
        public PlayerGroupLock load(Long playerGroup) throws Exception {
            return new PlayerGroupLock(playerGroup);
        }

    });

    public static class PlayerGroupLock implements Comparable<PlayerGroupLock> {
        final public ReentrantLock lock = new ReentrantLock();
        final public Long playerGroup;

        public PlayerGroupLock(Long playerGroup) {
            this.playerGroup = playerGroup;
        }

        @Override
        public int compareTo(PlayerGroupLock o) {
            return playerGroup.compareTo(o.playerGroup);
        }
    }

    @Override
    public void lock(long player) {
        PlayerGroupLock playerGroupLock = getLock(player);
        if (!playerGroupLock.lock.tryLock()) {
            try {
                if (!playerGroupLock.lock.tryLock(500, TimeUnit.NANOSECONDS)) {
                    playerGroupLock.lock.lock();
                }
            } catch (InterruptedException e) {
                throw GogomayaException.fromError(GogomayaError.PlayerLockAcquireFailure);
            }
        }
    }

    @Override
    public void lock(Collection<Long> players) {
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
    public void unlock(Collection<Long> players) {
        unlock(getLocks(players));
    }

    private void unlock(TreeSet<PlayerGroupLock> groupLocks) {
        for (PlayerGroupLock acquiredLock : groupLocks)
            acquiredLock.lock.unlock();
    }

    @Override
    public void unlock(long player) {
        PlayerGroupLock playerGroupLock = getLock(player);
        playerGroupLock.lock.unlock();
    }

    private TreeSet<PlayerGroupLock> getLocks(Collection<Long> players) {
        TreeSet<PlayerGroupLock> locks = new TreeSet<PlayerGroupLock>();
        for (Long player : players)
            locks.add(getLock(player));
        return locks;
    }

    private PlayerGroupLock getLock(long player) {
        PlayerGroupLock playerGroupLock = null;
        try {
            playerGroupLock = LOCKS.get((player >> LOCKS_PER_USER));
        } catch (ExecutionException e) {
            throw GogomayaException.fromError(GogomayaError.ServerCacheError);
        }
        return playerGroupLock;
    }

}
