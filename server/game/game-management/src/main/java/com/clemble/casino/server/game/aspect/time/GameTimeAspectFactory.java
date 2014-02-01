package com.clemble.casino.server.game.aspect.time;

import org.springframework.core.Ordered;

import com.clemble.casino.game.construct.ServerGameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspect;
import com.clemble.casino.server.game.aspect.GameAspectFactory;

public class GameTimeAspectFactory implements GameAspectFactory<GameManagementEvent> {

    final private GameEventTaskExecutor eventTaskExecutor;

    public GameTimeAspectFactory(GameEventTaskExecutor eventTaskExecutor) {
        this.eventTaskExecutor = eventTaskExecutor;
    }

    @Override
    public GameAspect<GameManagementEvent> construct(ServerGameInitiation initiation) {
        return new GameTimeAspect(initiation.getSession(), initiation.getConfiguration(), initiation.getContext(), eventTaskExecutor);
    }

    @Override
    public int getOrder(){
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
