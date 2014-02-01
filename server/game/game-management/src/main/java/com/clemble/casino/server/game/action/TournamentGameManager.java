package com.clemble.casino.server.game.action;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.TournamentGameRecord;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public class TournamentGameManager implements GameManager<TournamentGameRecord> {

    @Override
    public TournamentGameRecord getRecord() {
        return null;
    }

    @Override
    public GameManagementEvent process(GameSessionKey sessionKey, GameAction action) {
        return null;
    }

}
