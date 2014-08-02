package com.clemble.casino.server.event.player;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.server.event.SystemEvent;

/**
 * Created by mavarazy on 7/4/14.
 */
// TODO remove this logic moved to SOCIAL completely no need to duplicate
public interface SystemPlayerRegisteredEvent extends SystemEvent, PlayerAware {
}
