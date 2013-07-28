package com.gogomaya.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.rule.time.TimeRule;
import com.gogomaya.server.player.PlayerAware;

public class PlayerTimeTracker implements PlayerAware, Comparable<PlayerTimeTracker> {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1732050292805695127L;

    final public static long DEFAULT_BREACH_TIME = Long.MAX_VALUE;

    final private long playerId;
    final private TimeRule timeRule;

    private long moveStartTime;
    private long totalSpentTime;
    private long breachTime;

    public PlayerTimeTracker(long playerId, TimeRule timeRule) {
        this.playerId = playerId;
        this.timeRule = checkNotNull(timeRule);
        this.breachTime = Long.MAX_VALUE;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    /**
     * Marks that move is expected from the user.
     * If it was marked before, no action taken
     */
    public void markToMove() {
        if (breachTime == DEFAULT_BREACH_TIME) {
            this.moveStartTime = System.currentTimeMillis();
            this.breachTime = System.currentTimeMillis() + timeRule.getBreachTime(totalSpentTime);
        }
    }

    /**
     * Marks player, as already moved, all subsequent calls are ignored, until it's marked to move
     */
    public void markMoved() {
        if (breachTime != DEFAULT_BREACH_TIME) {
            this.totalSpentTime += System.currentTimeMillis() - moveStartTime;
            this.breachTime = DEFAULT_BREACH_TIME;
        }
    }

    public void appendBreachEvent(Collection<ClientEvent> events) {
        if (System.currentTimeMillis() >= breachTime)
            events.add(timeRule.toTimeBreachedEvent(playerId));
    }

    public long getBreachTime() {
        return breachTime;
    }

    @Override
    public int compareTo(PlayerTimeTracker o) {
        return Long.compare(breachTime, o.breachTime);
    }

    @Override
    public int hashCode() {
        return (int) (playerId ^ (playerId >>> 32));
    }

    @Override
    public boolean equals(Object obj) {
        return playerId == ((PlayerAware) obj).getPlayerId();
    }

}
