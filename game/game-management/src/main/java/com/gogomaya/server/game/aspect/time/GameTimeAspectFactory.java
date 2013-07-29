package com.gogomaya.server.game.aspect.time;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameEventTaskExecutor;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.game.construct.GameInitiation;

public class GameTimeAspectFactory implements GameAspectFactory {

    final private GameEventTaskExecutor eventTaskExecutor;

    public GameTimeAspectFactory(GameEventTaskExecutor eventTaskExecutor) {
        this.eventTaskExecutor = eventTaskExecutor;
    }

    @Override
    public <T extends GameState> GameAspect<T> construct(GameInitiation initiation) {
        SessionTimeTask sessionTimeTracker = new SessionTimeTask(initiation);
        eventTaskExecutor.schedule(sessionTimeTracker);
        return new GameTimeAspect<>(sessionTimeTracker, eventTaskExecutor);
    }

}
