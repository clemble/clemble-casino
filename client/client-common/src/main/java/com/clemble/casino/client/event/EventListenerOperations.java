package com.clemble.casino.client.event;

import java.io.Closeable;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.player.PlayerAware;

public interface EventListenerOperations extends PlayerAware, Closeable {

    public EventListenerController subscribe(EventListener listener);

    public EventListenerController subscribe(GameSessionKey sessionKey, EventListener listener);

    public EventListenerController subscribe(EventSelector selector, EventListener listener);

    public EventListenerController subscribe(String channel, EventListener listener);

    public EventListenerController subscribe(String channel, EventSelector selector, EventListener listener);

    public boolean isAlive();

    @Override
    public void close();

}
