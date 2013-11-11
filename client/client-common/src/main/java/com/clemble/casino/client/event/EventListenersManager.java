package com.clemble.casino.client.event;

public interface EventListenersManager {

    public void subscribe(EventListener listener);

    public void subscribe(EventSelector selector, EventListener listener);

}
