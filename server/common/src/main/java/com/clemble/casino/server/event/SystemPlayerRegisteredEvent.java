package com.clemble.casino.server.event;

import com.clemble.casino.player.PlayerAware;

/**
 * Created by mavarazy on 7/4/14.
 */
// TODO remove this logic moved to SOCIAL completely no need to duplicate
public interface SystemPlayerRegisteredEvent extends SystemEvent, PlayerAware {
}
