package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameProcessor;
import com.clemble.casino.game.TournamentGameRecord;
import com.clemble.casino.game.event.server.GameManagementEvent;

/**
 * Created by mavarazy on 12/02/14.
 */
public class TournamentGameProcessor implements GameProcessor<TournamentGameRecord, Event> {

    public GameManagementEvent process(TournamentGameRecord session, Event action) {
        return null;
    }

}
