package com.clemble.casino.server.game.aspect.time;

import org.springframework.core.Ordered;

import com.clemble.casino.game.MatchGameContext;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.game.specification.MatchGameConfiguration;
import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.MatchGameAspectFactory;

public class GameTimeAspectFactory implements MatchGameAspectFactory<GameManagementEvent> {

    final private GameEventTaskExecutor eventTaskExecutor;

    public GameTimeAspectFactory(GameEventTaskExecutor eventTaskExecutor) {
        this.eventTaskExecutor = eventTaskExecutor;
    }

    @Override
    public GameAspect<GameManagementEvent> construct(MatchGameConfiguration configuration, MatchGameContext context) {
        return new GameTimeAspect(context.getSession(), configuration, context, eventTaskExecutor);
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
