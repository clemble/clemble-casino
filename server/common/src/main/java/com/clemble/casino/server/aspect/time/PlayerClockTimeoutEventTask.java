package com.clemble.casino.server.aspect.time;

import com.clemble.casino.lifecycle.configuration.rule.time.MoveTimeRule;
import com.clemble.casino.lifecycle.configuration.rule.time.PlayerClock;
import com.clemble.casino.lifecycle.configuration.rule.time.TotalTimeRule;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Date;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 13/10/14.
 */
public class PlayerClockTimeoutEventTask implements PlayerAware, Comparable<PlayerClockTimeoutEventTask> {

    /**
     * Generated
     */
    private static final long serialVersionUID = 1732050292805695127L;

    final private static long MOVE_TIMEOUT = 5_000;

    final private String player;
    final private String key;
    final private PlayerClock clock;
    final private MoveTimeRule moveRule;
    final private TotalTimeRule totalRule;

    final Function<String, SystemEvent> eventFactory;
    final private SystemNotificationService notificationService;

    public PlayerClockTimeoutEventTask(
            String key,
            String player,
            PlayerClock playerClock,
            MoveTimeRule moveRule,
            TotalTimeRule totalRule,
            SystemNotificationService notificationService,
            Function<String, SystemEvent> eventFactory) {
        this.clock = checkNotNull(playerClock);
        this.player = player;
        this.key = checkNotNull(key);
        this.moveRule = checkNotNull(moveRule);
        this.totalRule = checkNotNull(totalRule);
        this.notificationService = checkNotNull(notificationService);
        this.eventFactory = eventFactory;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    /**
     * Marks that move is expected from the user.
     * If it was marked before, no action taken
     */
    public void start() {
        long moveStart = System.currentTimeMillis() + MOVE_TIMEOUT;
        long moveBreachTime = moveStart + moveRule.getLimit();
        long totalBreachTime = moveStart + totalRule.getLimit() - clock.getTimeSpent();
        clock.start(moveStart, Math.min(moveBreachTime, totalBreachTime), totalBreachTime, totalRule.getPunishment());

        SystemAddJobScheduleEvent addJobScheduleEvent = new SystemAddJobScheduleEvent(
            key,
            player,
            eventFactory.apply(key),
            new Date(clock.getBreachTime()));
        this.notificationService.send(addJobScheduleEvent);
    }

    /**
     * Marks player, as already moved, all subsequent calls are ignored, until it's marked to move
     */
    public void stop() {
        clock.stop();

        SystemRemoveJobScheduleEvent removeJobScheduleEvent = new SystemRemoveJobScheduleEvent(key, player);
        this.notificationService.send(removeJobScheduleEvent);
    }

    @Override
    public int compareTo(PlayerClockTimeoutEventTask o) {
        return Long.compare(clock.getBreachTime(), o.clock.getBreachTime());
    }

}
