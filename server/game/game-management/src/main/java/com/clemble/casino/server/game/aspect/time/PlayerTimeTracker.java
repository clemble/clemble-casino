package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.clemble.casino.game.GamePlayerClock;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.rule.time.TimeRule;
import com.clemble.casino.player.PlayerAware;

public class PlayerTimeTracker implements PlayerAware, Comparable<PlayerTimeTracker> {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1732050292805695127L;

    final private static long MOVE_TIMEOUT = 5_000;

    final private TimeRule timeRule;
    final private GamePlayerClock playerClock;

    public PlayerTimeTracker(GamePlayerClock playerClock, TimeRule timeRule) {
        this.timeRule = checkNotNull(timeRule);
        this.playerClock = playerClock;
    }

    @Override
    public String getPlayer() {
        return playerClock.getPlayer();
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

    public void appendBreachEvent(Collection<GameAction> events) {
        if (timeRule.timeUntilBreach(playerClock) <= 0)
            events.add(timeRule.toTimeBreachedEvent(playerClock.getPlayer()));
    }

    @Override
    public int compareTo(PlayerTimeTracker o) {
        return Long.compare(timeRule.timeUntilBreach(playerClock), timeRule.timeUntilBreach(o.playerClock));
    }

}
