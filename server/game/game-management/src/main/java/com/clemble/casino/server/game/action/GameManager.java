package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.event.server.GameManagementEvent;

public interface GameManager<R extends GameRecord> {
    
    public R getRecord();

    public GameManagementEvent process(GameSessionKey sessionKey, Event action);

}
