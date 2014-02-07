package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.specification.PotGameConfiguration;

public interface PotGameAspectFactory <T extends Event> extends GameAspectFactory<T, PotGameContext, PotGameConfiguration> {

    public GameAspect<T> construct(PotGameConfiguration initiation, PotGameContext potContext);

}
