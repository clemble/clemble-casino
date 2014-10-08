package com.clemble.casino.server.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.configuration.rule.time.PlayerClock;
import com.clemble.casino.lifecycle.configuration.rule.time.TimeRule;
import com.clemble.casino.player.PlayerAware;

public class PlayerTimeTracker implements PlayerAware, Comparable<PlayerTimeTracker> {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1732050292805695127L;

    final private static long MOVE_TIMEOUT = 5_000;

    final private String player;
    final private TimeRule timeRule;
    final private PlayerClock playerClock;

    public PlayerTimeTracker(String player, PlayerClock playerClock, TimeRule timeRule) {
        this.timeRule = checkNotNull(timeRule);
        this.playerClock = playerClock;
        this.player = player;
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
        playerClock.markToMove(MOVE_TIMEOUT);
    }

    /**
     * Marks player, as already moved, all subsequent calls are ignored, until it's marked to move
     */
    public void markMoved() {
        playerClock.markMoved();
    }

    public long getBreachTime(){
        long timeUntilBreach = timeRule.timeUntilBreach(playerClock);
        return timeUntilBreach < Long.MAX_VALUE ? System.currentTimeMillis() + timeUntilBreach : timeUntilBreach;
    }

    public void appendBreachEvent(Collection<Event> events) {
        if (timeRule.timeUntilBreach(playerClock) <= 0)
            events.add(timeRule.toTimeBreachedEvent(player));
    }

    @Override
    public int compareTo(PlayerTimeTracker o) {
        return Long.compare(timeRule.timeUntilBreach(playerClock), timeRule.timeUntilBreach(o.playerClock));
    }

}
