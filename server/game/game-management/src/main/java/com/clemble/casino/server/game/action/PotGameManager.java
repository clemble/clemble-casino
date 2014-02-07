package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameProcessor;
import com.clemble.casino.game.PotGameRecord;
import com.clemble.casino.game.event.server.GameManagementEvent;

public class PotGameManager implements GameManager<PotGameRecord> {

    final private PotGameRecord record;
    final private GameProcessor<PotGameRecord, Event> potProcessor;

    public PotGameManager(PotGameRecord record, GameProcessor<PotGameRecord, Event> potProcessor) {
        this.record = record;
        this.potProcessor = potProcessor;
    }

    @Override
    public PotGameRecord getRecord() {
        return record;
    }

    @Override
    public GameManagementEvent process(Event action) {
        // Step 1. Processing event with the manager
        return potProcessor.process(record, action);
    }

}
