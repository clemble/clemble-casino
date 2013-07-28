package com.gogomaya.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameEventTaskExecutor;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class GameTimeAspect<State extends GameState> implements GameAspect<State> {

    final private SessionTimerTask sessionTimeTracker;
    final private GameEventTaskExecutor gameEventTaskExecutor;

    public GameTimeAspect(SessionTimerTask sessionTimeTracker, GameEventTaskExecutor gameEventTaskExecutor) {
        this.sessionTimeTracker = checkNotNull(sessionTimeTracker);
        this.gameEventTaskExecutor = checkNotNull(gameEventTaskExecutor);

    }

    public SessionTimerTask getSessionTimeTracker() {
        return sessionTimeTracker;
    }

    @Override
    public void beforeMove(GameSession<State> session, ClientEvent move) {
        if (sessionTimeTracker.markMoved(move)) {
            gameEventTaskExecutor.reschedule(sessionTimeTracker);
        }
    }

    @Override
    public Collection<GameServerEvent<State>> afterMove(GameSession<State> session, Collection<GameServerEvent<State>> events) {
        if (session.getState().complete()) {
            gameEventTaskExecutor.cancel(sessionTimeTracker);
        } else {
            for (ClientEvent nextMove : session.getState().getActionLatch().getActions()) {
                sessionTimeTracker.markToMove(nextMove);
            }
            gameEventTaskExecutor.reschedule(sessionTimeTracker);
        }
        return events;
    }

}
