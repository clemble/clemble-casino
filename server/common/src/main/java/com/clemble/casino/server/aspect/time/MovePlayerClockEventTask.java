package com.clemble.casino.server.aspect.time;

import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.configuration.rule.time.MovePlayerClock;
import com.clemble.casino.lifecycle.configuration.rule.time.MoveTimeRule;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.executor.EventTask;
import org.springframework.scheduling.TriggerContext;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Created by mavarazy on 13/10/14.
 */
public class MovePlayerClockEventTask implements EventTask, PlayerAware {

    final private String key;
    final private String player;
    final private MoveTimeRule timeRule;
    final private MovePlayerClock moveClock;

    public MovePlayerClockEventTask(
        String key,
        String player,
        MoveTimeRule timeRule,
        MovePlayerClock moveClock) {
        this.key = key;
        this.player = player;
        this.timeRule = timeRule;
        this.moveClock = moveClock;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        return new Date(moveClock.getBreachTime());
    }

    @Override
    public Collection<? extends Event> execute() {
        return Collections.singletonList(timeRule.getPunishment().toBreachEvent(key, player));
    }

}
