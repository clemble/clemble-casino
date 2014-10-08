package com.clemble.casino.server.game.aspect;

import org.springframework.core.PriorityOrdered;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;

public interface GameAspectFactory<T extends Event, GC extends GameContext<?>, GI extends GameConfiguration> extends PriorityOrdered {

    public GameAspect<T> construct(GI configuration, GC context);

}
