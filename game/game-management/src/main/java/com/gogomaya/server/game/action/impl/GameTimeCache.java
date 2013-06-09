package com.gogomaya.server.game.action.impl;

import java.util.HashMap;

public class GameTimeCache {

    final private HashMap<Long, Long> moveTimeCache = new HashMap<Long, Long>();
    final private HashMap<Long, Long> totalTimeCache = new HashMap<Long, Long>();

    public void markStarted(long player) {
        // Step 1. Updating time cache only if it's not set
        if (moveTimeCache.get(player) == null)
            moveTimeCache.put(player, System.currentTimeMillis());
    }

    public void markEnded(long player) {
        // Step 1. Updating move and total time
        long moveTime = getTimeSinceMoveStart(player);
        Long totalAmmount = totalTimeCache.get(player);
        // Step 2. Updating cache value
        moveTimeCache.put(player, null);
        totalTimeCache.put(player, totalAmmount == null ? moveTime : (totalAmmount + moveTime));
    }

    public long getTimeSinceMoveStart(long player) {
        // Step 1. Checking if time for last moved marked
        Long time = moveTimeCache.get(player);
        // Step 2. Calculating time spent
        return time == null ? 0 : System.currentTimeMillis() - time;
    }

    public long getTotalTime(long player) {
        // Step 1. Total time is the time since the last move + current move time
        return totalTimeCache.get(player) + getTimeSinceMoveStart(player);
    }

}
