package com.clemble.casino.server.game.aspect;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.game.GameSession;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.action.GameAction;
import com.clemble.casino.game.event.server.GameManagementEvent;

public interface GameAspect extends EventListener {

    public EventSelector getSelector();

}
