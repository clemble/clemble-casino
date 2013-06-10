package com.gogomaya.server.game.rule.time.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import com.gogomaya.server.game.action.SessionAware;
import com.gogomaya.server.game.rule.time.MoveTimeRule;
import com.gogomaya.server.game.rule.time.TimeBreachPunishment;
import com.gogomaya.server.game.rule.time.TotalTimeRule;
import com.gogomaya.server.player.PlayerAware;

public class TimeTask implements SessionAware, Comparable<TimeTask> {

    final private long session;

    final private MoveTimeRule moveTimeRule;
    final private TotalTimeRule totalTimeRule;

    final private HashMap<Long, Long> lastMoveTime = new HashMap<Long, Long>();
    final private HashMap<Long, Long> totalTime = new HashMap<Long, Long>();

    final PriorityQueue<TimeBreach> timeBreach = new PriorityQueue<TimeBreach>();

    public static class TimeBreach implements Comparable<TimeBreach>, PlayerAware {

        /**
         * Generated 10/06/13
         */
        private static final long serialVersionUID = -8537249165242972837L;

        final private long breachTime;
        final private long playerId;
        final private TimeBreachPunishment breachPunishment;

        public TimeBreach(long playerId, long breachTime, TimeBreachPunishment breachPunishment) {
            this.breachTime = breachTime;
            this.breachPunishment = breachPunishment;
            this.playerId = playerId;
        }

        public TimeBreachPunishment getBreachPunishment() {
            return breachPunishment;
        }

        @Override
        public long getPlayerId() {
            return playerId;
        }

        public boolean breached() {
            return System.currentTimeMillis() >= breachTime;
        }

        public long getBreachTime() {
            return breachTime;
        }

        @Override
        public int compareTo(TimeBreach o) {
            return (int) (getBreachTime() - o.getBreachTime());
        }

    }

    public static class MoveTimeBreach extends TimeBreach {

        /**
         * Generated 10/06/13
         */
        private static final long serialVersionUID = 5205762297043841246L;

        public MoveTimeBreach(final long playerId, final MoveTimeRule moveTime) {
            super(playerId, System.currentTimeMillis() + moveTime.getLimit(), moveTime.getPunishment());
        }
    }

    public static class TotalTimeBreach extends TimeBreach {

        /**
         * Generated 10/06/13
         */
        private static final long serialVersionUID = -5828124513275245169L;

        public TotalTimeBreach(final long playerId, final long timeSpent, final TotalTimeRule totalTimeRule) {
            super(playerId, System.currentTimeMillis() + (totalTimeRule.getLimit() - timeSpent), totalTimeRule.getPunishment());
        }

    }

    public TimeTask(final long session, final MoveTimeRule moveTimeRule, final TotalTimeRule totalTimeRule) {
        this.session = session;
        this.moveTimeRule = moveTimeRule;
        this.totalTimeRule = totalTimeRule;
    }

    public void markStarted(long player) {
        // Step 1. Updating time cache only if it's not set
        if (lastMoveTime.get(player) == null) {
            lastMoveTime.put(player, System.currentTimeMillis());

            timeBreach.add(new MoveTimeBreach(player, moveTimeRule));
            timeBreach.add(new TotalTimeBreach(player, totalTime.get(player), totalTimeRule));
        }
    }

    public TimeBreach markEnded(long player) {
        // Step 1. Updating move and total time
        Long time = lastMoveTime.remove(player);
        if (time != null) {
            long currentMove = System.currentTimeMillis() - time;
            // Step 2. Updating cache value
            Long currentPlayerTotal = totalTime.get(player);
            totalTime.put(player, currentPlayerTotal == null ? currentMove : (currentPlayerTotal + currentMove));
        }
        // Step 3. Checking current state of the user
        Iterator<TimeBreach> timeLimits = timeBreach.iterator();
        while (timeLimits.hasNext()) {
            TimeBreach breach = timeLimits.next();
            if (breach.getPlayerId() == player) {
                timeLimits.remove();
                if (breach.breached()) {
                    return breach;
                }
            }
        }
        return null;
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

    public long getCheckTime() {
        TimeBreach breach = timeBreach.peek();
        return breach != null ? breach.getBreachTime() : Long.MAX_VALUE;
    }

    @Override
    public int compareTo(TimeTask o) {
        return (int) (getCheckTime() - o.getCheckTime());
    }

    @Override
    public int hashCode() {
        return (int) (session ^ (session >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof TimeTask) && session == ((TimeTask) obj).session;
    }

}
