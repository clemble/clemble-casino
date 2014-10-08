package com.clemble.casino.server.game.aspect;

import com.clemble.casino.game.lifecycle.management.GameState;
import com.clemble.casino.server.aspect.ClembleAspectFactory;
import org.springframework.core.PriorityOrdered;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.GameContext;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;

public interface GameAspectFactory<T extends Event, S extends GameState, C extends GameConfiguration> extends ClembleAspectFactory<T, C, S> {

    public GameAspect<T> construct(C configuration, S state);

}
