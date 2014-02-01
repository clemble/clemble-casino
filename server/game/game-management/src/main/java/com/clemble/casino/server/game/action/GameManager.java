package com.clemble.casino.server.game.action;

import com.clemble.casino.game.GameRecord;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.construct.GameInitiation;
import com.clemble.casino.game.event.server.GameManagementEvent;

public interface GameManager<R extends GameRecord> {

    public R start(GameInitiation initiation);

    public GameManagementEvent process(GameSessionKey sessionKey, GameAction action);

}
