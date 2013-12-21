package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.rule.time.TimeRule;
import com.clemble.casino.player.PlayerAware;

public class PlayerTimeTracker implements PlayerAware, Comparable<PlayerTimeTracker> {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1732050292805695127L;

    final public static long DEFAULT_BREACH_TIME = Long.MAX_VALUE;

    final private String player;
    final private TimeRule timeRule;

    private long moveStartTime;
    private long totalSpentTime;
    private long breachTime;

    public PlayerTimeTracker(String player, TimeRule timeRule) {
        this.player = player;
        this.timeRule = checkNotNull(timeRule);
        this.breachTime = Long.MAX_VALUE;
    }

    @Override
    public String getPlayer() {
        return player;
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

    public void appendBreachEvent(Collection<GameAction> events) {
        if (System.currentTimeMillis() >= breachTime)
            events.add(timeRule.toTimeBreachedEvent(player));
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
        return (int) (player == null ? 0 : player.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        return player == ((PlayerAware) obj).getPlayer();
    }

}
