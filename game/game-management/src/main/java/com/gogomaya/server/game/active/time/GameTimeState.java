package com.gogomaya.server.game.active.time;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TotalTimeRule;

public class GameTimeState implements SessionAware, Comparable<GameTimeState> {

    final private long session;

    final private MoveTimeRule moveTimeRule;
    final private TotalTimeRule totalTimeRule;

    final private HashMap<Long, Long> lastMoveTime = new HashMap<Long, Long>();
    final private HashMap<Long, Long> totalTime = new HashMap<Long, Long>();

    final PriorityQueue<GameTimeBreach> timeBreach = new PriorityQueue<GameTimeBreach>();

    public GameTimeState(final long session, final MoveTimeRule moveTimeRule, final TotalTimeRule totalTimeRule) {
        this.session = session;
        this.moveTimeRule = moveTimeRule;
        this.totalTimeRule = totalTimeRule;
    }

    public void markStarted(long player) {
        // Step 1. Updating time cache only if it's not set
        if (lastMoveTime.get(player) == null) {
            lastMoveTime.put(player, System.currentTimeMillis());

            timeBreach.add(new GameTimeBreach(player, System.currentTimeMillis() + moveTimeRule.getLimit(), moveTimeRule));
            timeBreach.add(new GameTimeBreach(player, System.currentTimeMillis() + (getTotalTime(player) - totalTime.get(player)), totalTimeRule));
        }
    }

    public void markEnded(long player) {
        // Step 1. Updating move and total time
        Long time = lastMoveTime.remove(player);
        if (time != null) {
            long currentMove = System.currentTimeMillis() - time;
            // Step 2. Updating cache value
            Long currentPlayerTotal = totalTime.get(player);
            totalTime.put(player, currentPlayerTotal == null ? currentMove : (currentPlayerTotal + currentMove));
        }
        // Step 3. Checking current state of the user
        Iterator<GameTimeBreach> timeLimits = timeBreach.iterator();
        while (timeLimits.hasNext()) {
            if (timeLimits.next().getPlayerId() == player) {
                timeLimits.remove();
            }
        }
    }

    public GameTimeBreach peek() {
        return timeBreach.peek();
    }

    public long getMoveTime(long player) {
        // Step 1. Checking if time for last moved marked
        Long time = lastMoveTime.get(player);
        // Step 2. Calculating time spent
        return time == null ? 0 : System.currentTimeMillis() - time;
    }

    public long getTotalTime(long player) {
        // Step 1. Total time is the time since the last move + current move time
        return totalTime.get(player) + getMoveTime(player);
    }

    @Override
    public long getSession() {
        return session;
    }
    
    public boolean breached() {
        GameTimeBreach breach = timeBreach.peek();
        return breach != null ? breach.breached() : false;
    }

    public long getCheckTime() {
        GameTimeBreach breach = timeBreach.peek();
        return breach != null ? breach.getBreachTime() : Long.MAX_VALUE;
    }

    @Override
    public int compareTo(GameTimeState o) {
        return (int) (getCheckTime() - o.getCheckTime());
    }

    @Override
    public int hashCode() {
        return (int) (session ^ (session >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof GameTimeState) && session == ((GameTimeState) obj).session;
    }

}
