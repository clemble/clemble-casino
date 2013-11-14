package com.clemble.casino.client.event;

import com.clemble.casino.game.GameSessionKey;

public interface EventListenersManager {

    public EventListenerController subscribe(EventListener listener);

    public EventListenerController subscribe(GameSessionKey sessionKey, EventListener listener);

    public EventListenerController subscribe(EventSelector selector, EventListener listener);

}
