package com.clemble.casino.server.action;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.lifecycle.management.State;
import com.clemble.casino.server.aspect.ClembleAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mavarazy on 10/9/14.
 */
public class ClembleManager<R extends Event, T extends State<R, ?>> {
    final private static Logger LOG = LoggerFactory.getLogger(ClembleManager.class);

    final private ReentrantLock sessionLock = new ReentrantLock();

    final private Collection<ClembleAspect<?>> listenerArray;
    final private T state;

    public ClembleManager(T state, Collection<ClembleAspect<?>> listenerArray) {
        this.listenerArray = listenerArray;
        this.state = checkNotNull((T) state);
    }

    public T getState() {
        return state;
    }

    public R process(Event action) {
        // Step 1. Sanity check
        if (action == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveUndefined);
        // Step 1.1 Add check for ended games check
        // Step 2. Acquiring lock for the session, to exclude parallel processing
        sessionLock.lock();
        try {
            LOG.debug("Processing {}", action);
            // Step 1. Before move notification
            for (ClembleAspect listener : listenerArray)
                listener.onEvent(action);
            // Step 2. Processing in core
            R event = state.process(action);
            // Step 3 After move notification
            for (ClembleAspect listener : listenerArray)
                listener.onEvent(event);
            // Step 3. Returning game event
            return event;
        } finally {
            sessionLock.unlock();
        }
    }

}
