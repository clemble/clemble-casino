package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.lifecycle.configuration.RoundGameConfiguration;
import com.clemble.casino.game.lifecycle.management.RoundGameState;
import org.springframework.core.Ordered;

import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class RoundGameTimeAspectFactory implements RoundGameAspectFactory<GameManagementEvent> {

    final private EventTaskExecutor eventTaskExecutor;

    public RoundGameTimeAspectFactory(EventTaskExecutor eventTaskExecutor) {
        this.eventTaskExecutor = eventTaskExecutor;
    }

    @Override
    public GameAspect<GameManagementEvent> construct(RoundGameConfiguration configuration, RoundGameState roundState) {
        return new RoundGameTimeAspect(configuration, roundState.getContext(), eventTaskExecutor);
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
