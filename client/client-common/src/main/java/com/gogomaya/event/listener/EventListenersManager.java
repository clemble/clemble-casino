package com.gogomaya.event.listener;

public interface EventListenersManager {

    public void subscribe(EventListener listener);

    public void subscribe(EventSelector selector, EventListener listener);

}
