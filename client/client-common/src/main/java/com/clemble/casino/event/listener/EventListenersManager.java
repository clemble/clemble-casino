package com.clemble.casino.event.listener;

public interface EventListenersManager {

    public void subscribe(EventListener listener);

    public void subscribe(EventSelector selector, EventListener listener);

}
