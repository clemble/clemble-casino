package com.clemble.casino.server.event.game;

import com.clemble.casino.game.GameSessionAware;
import com.clemble.casino.server.event.SystemEvent;

/**
 * Created by mavarazy on 8/18/14.
 */
public interface SystemGameEvent extends SystemEvent, GameSessionAware {
}
