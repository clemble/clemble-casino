package com.gogomaya.server.game.aspect.time;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.gogomaya.server.game.SessionAware;

abstract public class TimeTracker implements SessionAware, Comparable<TimeTracker> {

    /**
     * Generated 20/07/13
     */
    private static final long serialVersionUID = -7295625809660173928L;

    final private long session;

    final private HashMap<Long, Long> lastMoveTime = new HashMap<Long, Long>();
    final private PriorityQueue<GameTimeBreach> timeBreach = new PriorityQueue<GameTimeBreach>();

    public TimeTracker(final long session) {
        this.session = session;
    }

    public void markStarted(long player) {
        // Step 1. Updating time cache only if it's not set
        if (lastMoveTime.get(player) == null) {
            lastMoveTime.put(player, System.currentTimeMillis());

            startTracking(player);
        }
    }

    public void markEnded(long player) {
        // Step 1. Updating move and total time
        Long time = lastMoveTime.remove(player);
        if (time != null) {
            // Step 2. Updating cache value
            stopTracking(player, System.currentTimeMillis() - time);
        }
        // Step 3. Checking current state of the user
        Iterator<GameTimeBreach> timeLimits = timeBreach.iterator();
        while (timeLimits.hasNext()) {
            if (timeLimits.next().getPlayerId() == player) {
                timeLimits.remove();
            }
        }
    }

    abstract public void startTracking(long player);

    abstract public void stopTracking(long player, long moveTime);

    public void add(GameTimeBreach breach) {
        timeBreach.add(breach);
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
    public int compareTo(TimeTracker o) {
        return (int) (getCheckTime() - o.getCheckTime());
    }

    @Override
    public int hashCode() {
        return (int) (session ^ (session >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TimeTracker) && session == ((TimeTracker) obj).session;
    }

}
