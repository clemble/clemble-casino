package com.clemble.casino.server.game.action;

import com.clemble.casino.game.MatchGameRecord;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public interface MatchGameProcessor {

    public GameManagementEvent process(final MatchGameRecord session, final GameAction move);

}
