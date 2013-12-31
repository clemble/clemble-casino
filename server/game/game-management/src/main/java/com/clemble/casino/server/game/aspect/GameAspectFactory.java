package com.clemble.casino.server.game.aspect;

import org.springframework.core.PriorityOrdered;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.construct.GameInitiation;

public interface GameAspectFactory<T extends Event> extends PriorityOrdered {

    public GameAspect<T> construct(GameInitiation initiation, GameContext construction);

}
