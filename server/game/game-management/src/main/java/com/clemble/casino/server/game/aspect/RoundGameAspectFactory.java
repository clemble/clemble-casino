package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.RoundGameContext;
import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundGameState;

public interface RoundGameAspectFactory<T extends Event> extends GameAspectFactory<T, RoundGameState, RoundGameConfiguration>{

    public GameAspect<T> construct(RoundGameConfiguration configuration, RoundGameState context);

}
