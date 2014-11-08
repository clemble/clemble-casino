package com.clemble.casino.server.executor;

import java.util.Collection;
import java.util.Date;

import com.clemble.casino.event.Event;
import org.springframework.scheduling.Trigger;

import com.clemble.casino.game.GameSessionAware;

public interface EventTask {

    public Collection<? extends Event> execute();

    public String getKey();

    public Date nextExecutionTime();
}
