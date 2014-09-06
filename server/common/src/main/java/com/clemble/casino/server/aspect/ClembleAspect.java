package com.clemble.casino.server.aspect;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventSelector;
import com.clemble.casino.event.Event;

/**
 * Created by mavarazy on 9/6/14.
 */
abstract public class ClembleAspect<T extends Event> implements EventListener<T> {

    final private EventSelector selector;

    public ClembleAspect(EventSelector selector){
        this.selector=selector==null?EventSelector.TRUE:selector;
    }

    @Override
    final public void onEvent(T event){
        if(selector.filter(event))
            doEvent(event);
    }

    abstract public void doEvent(T event);

    final public EventSelector getSelector(){
        return selector;
    }

}
