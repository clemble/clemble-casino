package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.configuration.MatchGameConfiguration;
import com.clemble.casino.server.game.action.MatchGameState;

public interface MatchGameAspectFactory<T extends Event> extends GameAspectFactory<T, MatchGameState, MatchGameConfiguration> {

    public GameAspect<T> construct(MatchGameConfiguration initiation, MatchGameState potContext);

}
