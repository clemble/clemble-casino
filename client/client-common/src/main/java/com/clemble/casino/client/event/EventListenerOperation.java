package com.clemble.casino.client.event;

import java.io.Closeable;

import com.clemble.casino.game.GameSessionKey;

public interface EventListenerOperation extends Closeable {

    public EventListenerController subscribe(EventListener listener);

    public EventListenerController subscribe(GameSessionKey sessionKey, EventListener listener);

    public EventListenerController subscribe(EventSelector selector, EventListener listener);

    @Override
    public void close();

}
