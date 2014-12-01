package com.clemble.casino.server.event.post;

import com.clemble.casino.post.PlayerPostAware;
import com.clemble.casino.server.event.SystemEvent;

/**
 * Created by mavarazy on 11/30/14.
 */
public interface SystemPostEvent extends SystemEvent, PlayerPostAware {
}
