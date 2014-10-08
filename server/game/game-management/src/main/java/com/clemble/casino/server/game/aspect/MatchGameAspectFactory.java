package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.MatchGameContext;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;

public interface MatchGameAspectFactory<T extends Event> extends GameAspectFactory<T, MatchGameContext, MatchGameConfiguration> {

    public GameAspect<T> construct(MatchGameConfiguration initiation, MatchGameContext potContext);

}
