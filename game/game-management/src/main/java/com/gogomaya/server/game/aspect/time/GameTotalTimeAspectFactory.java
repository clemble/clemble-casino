package com.gogomaya.server.game.aspect.time;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameEventTaskExecutor;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.aspect.GameAspectFactory;
import com.gogomaya.server.game.specification.GameSpecification;

public class GameTotalTimeAspectFactory implements GameAspectFactory {

    final private GameEventTaskExecutor eventTaskExecutor;

    public GameTotalTimeAspectFactory(GameEventTaskExecutor eventTaskExecutor) {
        this.eventTaskExecutor = eventTaskExecutor;
    }

    @Override
    public <T extends GameState> GameAspect<T> construct(GameSpecification gameSpecification) {
        SessionTimerTask sessionTimeTracker = new SessionTimerTask(null, gameSpecification.getTotalTimeRule());
        eventTaskExecutor.schedule(sessionTimeTracker);
        return new GameTimeAspect<>(sessionTimeTracker, eventTaskExecutor);
    }
}
