package com.clemble.casino.server.game.action;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.locks.ReentrantLock;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameProcessor;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.event.server.GameManagementEvent;

public class MatchGameManager implements GameManager<MatchGameRecord> {

    final private ReentrantLock sessionLock = new ReentrantLock();

    final private GameProcessor<MatchGameRecord, Event> processor;
    final private MatchGameRecord session;

    public MatchGameManager(GameProcessor<MatchGameRecord, Event> processor, MatchGameRecord session) {
        this.processor = checkNotNull(processor);
        this.session = checkNotNull(session);
    }
    
    public MatchGameRecord getRecord() {
        return session;
    }

    @Override
    public GameManagementEvent process(GameSessionKey sessionKey, Event action) {
        // Step 1. Sanity check
        if (action == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayMoveUndefined);
        switch (session.getSessionState()) {
        case finished:
            throw ClembleCasinoException.fromError(ClembleCasinoError.GamePlayGameEnded);
        default:
            break;
        }
        // Step 2. Acquiring lock for the session, to exclude parallel processing
        sessionLock.lock();;
        try {
            // Step 3. Retrieving game processor based on session identifier
            return processor.process(session, action);
        } finally {
            sessionLock.unlock();
        }
    }

}
