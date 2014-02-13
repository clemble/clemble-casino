package com.clemble.casino.server.game.action;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameProcessor;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.event.server.GameManagementEvent;

import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkNotNull;

public class GameManager<R extends GameRecord> {


    final private ReentrantLock sessionLock = new ReentrantLock();

    final private GameProcessor<R, Event> processor;
    final private R record;

    public GameManager(GameProcessor<R, Event> processor, R session) {
        this.processor = checkNotNull(processor);
        this.record = checkNotNull(session);
    }

    public R getRecord() {
        return record;
    }

    public GameManagementEvent process(Event action) {
        // Step 1. Sanity check
        if (action == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveUndefined);
        switch (record.getSessionState()) {
            case finished:
                throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayGameEnded);
            default:
                break;
        }
        // Step 2. Acquiring lock for the session, to exclude parallel processing
        sessionLock.lock();
        try {
            // Step 3. Retrieving game processor based on session identifier
            return processor.process(record, action);
        } finally {
            sessionLock.unlock();
        }
    }

}
