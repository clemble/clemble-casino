package com.clemble.casino.server.game.aspect.time;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.client.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;
import com.clemble.casino.server.game.action.GameEventTaskExecutor;
import com.clemble.casino.server.game.aspect.GameAspect;

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
    public void beforeMove(State state, GameAction move) {
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
    public void afterMove(State state, GameManagementEvent<State> events) {
        // Step 1. To check if we need rescheduling, first calculate time before
        long breachTimeBeforeMove = sessionTimeTracker.getBreachTime();
        // Step 2. Updating sessionTimeTracker
        for (GameAction nextMove : state.getActionLatch().<GameAction>getActions()) {
            sessionTimeTracker.markToMove(nextMove);
        }
        // Step 3. Re scheduling if needed
        if (sessionTimeTracker.getBreachTime() != breachTimeBeforeMove) {
            gameEventTaskExecutor.reschedule(sessionTimeTracker);
        }
    }

    @Override
    public void afterGame(GameSession<State> state, GameManagementEvent<State> events) {
        gameEventTaskExecutor.cancel(sessionTimeTracker);
    }

}
