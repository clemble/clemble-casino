package com.clemble.casino.server.game.aspect;

import org.springframework.core.PriorityOrdered;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.specification.MatchGameConfiguration;

public interface MatchGameAspectFactory<T extends Event> extends GameAspectFactory<T, MatchGameContext, MatchGameConfiguration>{

    public GameAspect<T> construct(MatchGameConfiguration configuration, MatchGameContext context);

}
