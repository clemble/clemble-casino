package com.clemble.casino.server.game.action;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.PotGameRecord;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public class PotGameManager implements GameManager<PotGameRecord> {

    public PotGameManager() {
    }

    @Override
    public PotGameRecord getRecord() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameManagementEvent process(GameSessionKey sessionKey, GameAction action) {
        // TODO Auto-generated method stub
        return null;
    }

}
