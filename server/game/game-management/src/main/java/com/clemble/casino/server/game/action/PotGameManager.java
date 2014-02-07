package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.PotGameRecord;
import com.clemble.casino.game.event.server.GameManagementEvent;

public class PotGameManager implements GameManager<PotGameRecord> {

    final private PotGameRecord record;
    final private PotGameContext potContext;
    final private GameManager<PotGameRecord> manager;

    public PotGameManager(PotGameRecord record) {
        this.potContext = new PotGameContext(record.getSession(), null, null, 0);
        this.record = record;
        this.manager = null;
    }

    @Override
    public PotGameRecord getRecord() {
        return record;
    }

    @Override
    public GameManagementEvent process(GameSessionKey sessionKey, Event action) {
        // Step 1. Processing event with the manager
        manager.process(sessionKey, action);
        return null;
    }

}
