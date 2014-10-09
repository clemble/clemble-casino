package com.clemble.casino.server.game.aspect;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.configuration.GameConfiguration;
import com.clemble.casino.game.lifecycle.management.GameState;

/**
 * Created by mavarazy on 10/9/14.
 */
public interface GenericGameAspectFactory<T extends Event> extends GameAspectFactory<T, GameState, GameConfiguration>{
}
