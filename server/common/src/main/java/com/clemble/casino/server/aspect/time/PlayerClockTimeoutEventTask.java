package com.clemble.casino.server.aspect.time;

import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.configuration.rule.time.MoveTimeRule;
import com.clemble.casino.lifecycle.configuration.rule.time.PlayerClock;
import com.clemble.casino.lifecycle.configuration.rule.time.TotalTimeRule;
import com.clemble.casino.lifecycle.management.event.action.PlayerAction;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.executor.EventTask;
import com.clemble.casino.server.executor.EventTaskExecutor;
import org.springframework.scheduling.TriggerContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 13/10/14.
 */
public class PlayerClockTimeoutEventTask implements PlayerAware, Comparable<PlayerClockTimeoutEventTask>, EventTask {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1732050292805695127L;

    final private static long MOVE_TIMEOUT = 5_000;

    final private String player;
    final private String sessionKey;
    final private PlayerClock clock;
    final private MoveTimeRule moveRule;
    final private TotalTimeRule totalRule;
    final private EventTaskExecutor taskExecutor;

    public PlayerClockTimeoutEventTask(
            String sessionKey,
            String player,
            PlayerClock playerClock,
            MoveTimeRule moveRule,
            TotalTimeRule totalRule,
            EventTaskExecutor taskExecutor) {
        this.clock = checkNotNull(playerClock);
        this.player = player;
        this.sessionKey = checkNotNull(sessionKey);
        this.moveRule = checkNotNull(moveRule);
        this.totalRule = checkNotNull(totalRule);
        this.taskExecutor = checkNotNull(taskExecutor);
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getKey() {
        return sessionKey;
    }

    /**
     * Marks that move is expected from the user.
     * If it was marked before, no action taken
     */
    public void start() {
        long moveStart = System.currentTimeMillis() + MOVE_TIMEOUT;
        long moveBreachTime = moveStart + moveRule.getLimit();
        long totalBreachTime = moveStart + totalRule.getLimit() - clock.getTimeSpent();
        if (moveBreachTime > totalBreachTime) {
            clock.start(moveStart, totalBreachTime, totalRule.getPunishment());
        } else {
            clock.start(moveStart, moveBreachTime, totalRule.getPunishment());
        }
        taskExecutor.schedule(this);
    }

    /**
     * Marks player, as already moved, all subsequent calls are ignored, until it's marked to move
     */
    public void stop() {
        clock.stop();
        taskExecutor.cancel(this);
    }

    @Override
    public Collection<? extends Event> execute() {
        return Collections.singleton(new PlayerAction(sessionKey, player, clock.getPunishment().toBreachEvent()));
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        return new Date(clock.getBreachTime());
    }

    @Override
    public int compareTo(PlayerClockTimeoutEventTask o) {
        return Long.compare(clock.getBreachTime(), o.clock.getBreachTime());
    }

}
