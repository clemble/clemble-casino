package com.clemble.casino.server.game.aspect.time;

import com.clemble.casino.game.RoundGameContext;
import com.clemble.casino.game.configuration.RoundGameConfiguration;
import org.springframework.core.Ordered;

import com.clemble.casino.game.event.GameManagementEvent;
import com.clemble.casino.server.executor.EventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.RoundGameAspectFactory;

public class GameTimeAspectFactory implements RoundGameAspectFactory<GameManagementEvent> {

    final private EventTaskExecutor eventTaskExecutor;

    public GameTimeAspectFactory(EventTaskExecutor eventTaskExecutor) {
        this.eventTaskExecutor = eventTaskExecutor;
    }

    @Override
    public GameAspect<GameManagementEvent> construct(RoundGameConfiguration configuration, RoundGameContext context) {
        return new GameTimeAspect(context.getSessionKey(), configuration, context, eventTaskExecutor);
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
