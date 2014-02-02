package com.clemble.casino.server.game.aspect;

import org.springframework.core.PriorityOrdered;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.construct.GameInitiation;

public interface PotGameAspectFactory <T extends Event> extends PriorityOrdered {

    public GameAspect<T> construct(GameInitiation initiation, PotGameContext potContext);

}
