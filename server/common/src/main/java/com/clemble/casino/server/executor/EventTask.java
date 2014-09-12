package com.clemble.casino.server.executor;

import java.util.Collection;

import com.clemble.casino.event.Event;
import org.springframework.scheduling.Trigger;

import com.clemble.casino.game.GameSessionAware;

public interface EventTask extends Trigger {

    public Collection<? extends Event> execute();

    public String getKey();
}
