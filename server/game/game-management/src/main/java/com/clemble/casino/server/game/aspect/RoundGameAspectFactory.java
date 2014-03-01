package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.specification.RoundGameConfiguration;

public interface RoundGameAspectFactory<T extends Event> extends GameAspectFactory<T, RoundGameContext, RoundGameConfiguration>{

    public GameAspect<T> construct(RoundGameConfiguration configuration, RoundGameContext context);

}
