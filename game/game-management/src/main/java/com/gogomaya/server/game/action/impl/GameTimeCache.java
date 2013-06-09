package com.gogomaya.server.game.action.impl;

import java.util.HashMap;

public class GameTimeCache {

    final private HashMap<Long, Long> moveTime = new HashMap<Long, Long>();
    final private HashMap<Long, Long> totalTime = new HashMap<Long, Long>();

    public void markStarted(long player) {
        // Step 1. Updating time cache only if it's not set
        if (moveTime.get(player) == null)
            moveTime.put(player, System.currentTimeMillis());
    }

    public void markEnded(long player) {
        // Step 1. Updating move and total time
        long currentMove = getMoveTime(player);
        moveTime.put(player, null);
        // Step 2. Updating cache value
        Long currentPlayerTotal = totalTime.get(player);
        totalTime.put(player, currentPlayerTotal == null ? currentMove : (currentPlayerTotal + currentMove));
    }

    public long getMoveTime(long player) {
        // Step 1. Checking if time for last moved marked
        Long time = moveTime.get(player);
        // Step 2. Calculating time spent
        return time == null ? 0 : System.currentTimeMillis() - time;
    }

    public long getTotalTime(long player) {
        // Step 1. Total time is the time since the last move + current move time
        return totalTime.get(player) + getMoveTime(player);
    }

}
