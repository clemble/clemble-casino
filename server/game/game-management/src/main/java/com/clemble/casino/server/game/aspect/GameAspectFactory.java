package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.construct.GameInitiation;

public interface GameAspectFactory<T extends Event, GC extends PotGameContext> {

    public GameAspect<T> construct(GameInitiation initiation, GC potContext);

}
