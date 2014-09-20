package com.clemble.casino.server.player.notification;

import com.clemble.casino.server.event.SystemEvent;

public interface SystemEventListener<T extends SystemEvent>{

    final public static String EXCHANGE = "clemble.system";

    public void onEvent(T event);

    public String getChannel();

    /**
     * Naming convension for the Queue is the name of the Channel > + name of the component
     * @return
     */
    public String getQueueName();

}
