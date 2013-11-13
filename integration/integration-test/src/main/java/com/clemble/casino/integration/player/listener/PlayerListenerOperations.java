package com.clemble.casino.integration.player.listener;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerController;
import com.clemble.casino.player.security.PlayerSession;

public interface PlayerListenerOperations {

    public EventListenerController listen(PlayerSession playerSession, EventListener listener);

    public EventListenerController listen(PlayerSession playerSession, EventListener listener, ListenerChannel channel);

}
