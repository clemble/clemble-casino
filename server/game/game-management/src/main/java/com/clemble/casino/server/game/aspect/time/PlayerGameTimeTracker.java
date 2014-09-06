package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.rule.time.PlayerClock;
import com.clemble.casino.rule.time.TimeRule;
import com.clemble.casino.server.game.action.ScheduledGameAction;
import com.clemble.casino.server.game.action.ScheduledGameActionExecutor;

public class PlayerGameTimeTracker {

    final private static long MOVE_TIMEOUT = 5_000;

    final private TimeRule timeRule;
    final private PlayerClock playerClock;

    final private ScheduledGameAction action;
    final private ScheduledGameActionExecutor scheduledActionExecutor;

    public PlayerGameTimeTracker( 
            PlayerClock playerClock,
            ScheduledGameAction action,
            TimeRule timeRule,
            ScheduledGameActionExecutor scheduledActionExecutor) {
        this.timeRule = checkNotNull(timeRule);
        this.playerClock = playerClock;
        this.scheduledActionExecutor = checkNotNull(scheduledActionExecutor);
        this.action = action;
    }

    /**
     * Marks that move is expected from the user.
     * If it was marked before, no action taken
     */
    public void markToMove() {
        playerClock.markToMove(MOVE_TIMEOUT);
        action.setScheduled(timeRule.breachDate(playerClock));
        scheduledActionExecutor.schedule(action);
    }

    /**
     * Marks player, as already moved, all subsequent calls are ignored, until it's marked to move
     */
    public void markMoved() {
        playerClock.markMoved();
        scheduledActionExecutor.cancel(action);
    }

}
