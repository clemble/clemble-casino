package com.gogomaya.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.event.ClientEvent;
import com.gogomaya.server.game.GameSession;
import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.action.GameEventTaskExecutor;
import com.gogomaya.server.game.aspect.GameAspect;
import com.gogomaya.server.game.event.server.GameServerEvent;

public class GameTimeAspect<State extends GameState> implements GameAspect<State> {

    final private SessionTimeTask sessionTimeTracker;
    final private GameEventTaskExecutor gameEventTaskExecutor;

    public GameTimeAspect(SessionTimeTask sessionTimeTracker, GameEventTaskExecutor gameEventTaskExecutor) {
        this.sessionTimeTracker = checkNotNull(sessionTimeTracker);
        this.gameEventTaskExecutor = checkNotNull(gameEventTaskExecutor);

    }

    public SessionTimeTask getSessionTimeTracker() {
        return sessionTimeTracker;
    }

    @Override
    public void beforeMove(State state, ClientEvent move) {
        // Step 1. To check if we need rescheduling, first calculate time before
        long breachTimeBeforeMove = sessionTimeTracker.getBreachTime();
        // Step 2. Updating sessionTimeTracker
        sessionTimeTracker.markMoved(move);
        // Step 3. Re scheduling if needed
        if (sessionTimeTracker.getBreachTime() != breachTimeBeforeMove) {
            gameEventTaskExecutor.reschedule(sessionTimeTracker);
        }
    }

    @Override
    public void afterMove(State state, GameServerEvent<State> events) {
        // Step 1. To check if we need rescheduling, first calculate time before
        long breachTimeBeforeMove = sessionTimeTracker.getBreachTime();
        // Step 2. Updating sessionTimeTracker
        for (ClientEvent nextMove : state.getActionLatch().getActions()) {
            sessionTimeTracker.markToMove(nextMove);
        }
        // Step 3. Re scheduling if needed
        if (sessionTimeTracker.getBreachTime() != breachTimeBeforeMove) {
            gameEventTaskExecutor.reschedule(sessionTimeTracker);
        }
    }

    @Override
    public void afterGame(GameSession<State> state, GameServerEvent<State> events) {
        gameEventTaskExecutor.cancel(sessionTimeTracker);
    }

}
