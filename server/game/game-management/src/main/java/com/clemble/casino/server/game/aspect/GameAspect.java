package com.clemble.casino.server.game.aspect;

import org.springframework.core.PriorityOrdered;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;

public interface GameAspect<T extends Event> extends EventListener<T> {

    public EventSelector getSelector();

}
