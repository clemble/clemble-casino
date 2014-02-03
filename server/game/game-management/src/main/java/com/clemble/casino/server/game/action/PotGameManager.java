package com.clemble.casino.server.game.action;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.PotGameContext;
import com.clemble.casino.game.PotGameRecord;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public class PotGameManager implements GameManager<PotGameRecord> {

    final private GameManager manager;
    final private PotGameRecord record;
    final private PotGameContext potContext;

    public PotGameManager(PotGameRecord record) {
        this.record = record;
        this.potContext = new PotGameContext(record.getSession(), null, null, 0);
        this.manager = null;
    }

    @Override
    public PotGameRecord getRecord() {
        return record;
    }

    @Override
    public GameManagementEvent process(GameSessionKey sessionKey, GameAction action) {
        // Step 1. Processing event with the manager
        manager.process(sessionKey, action);
        return null;
    }

}
