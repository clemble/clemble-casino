package com.clemble.casino.server.game.action;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameContext;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.GameManagementEvent;
import com.clemble.casino.server.game.aspect.GameAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameManager<GC extends GameContext> {

    final private static Logger LOG = LoggerFactory.getLogger(GameManager.class);

    final private ReentrantLock sessionLock = new ReentrantLock();

    final private Collection<GameAspect<?>> listenerArray;
    final private GameState<GC, Event> state;
    final private GameContext<?> context;

    public GameManager(GameContext<?> context, GameState<GC, ? extends Event> state, Collection<GameAspect<?>> listenerArray) {
        this.listenerArray = listenerArray;
        this.context = checkNotNull(context);
        this.state = checkNotNull((GameState<GC, Event>) state);
    }

    public GameContext<?> getContext(){
        return context;
    }

    public GameState<GC, Event> getState() {
        return state;
    }

    public GameManagementEvent process(Event action) {
        // Step 1. Sanity check
        if (action == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveUndefined);
        // Step 1.1 Add check for ended games check
        // Step 2. Acquiring lock for the session, to exclude parallel processing
        sessionLock.lock();
        try {
            LOG.debug("Processing {}", action);
            // Step 1. Before move notification
            for (GameAspect listener : listenerArray)
                listener.onEvent(action);
            // Step 2. Processing in core
            GameManagementEvent event = state.process(action);
            // Step 3 After move notification
            for (GameAspect listener : listenerArray)
                listener.onEvent(event);
            // Step 3. Returning game event
            return event;
        } finally {
            sessionLock.unlock();
        }
    }

}
