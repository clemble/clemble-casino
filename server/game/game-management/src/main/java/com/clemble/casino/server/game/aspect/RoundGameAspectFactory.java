package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;

public interface RoundGameAspectFactory<T extends Event> extends GameAspectFactory<T, RoundGameContext, RoundGameConfiguration>{

    public GameAspect<T> construct(RoundGameConfiguration configuration, RoundGameContext context);

}
