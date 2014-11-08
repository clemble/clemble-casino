package com.clemble.casino.server.event.schedule;

import com.clemble.casino.server.event.SystemEvent;

/**
 * Base class for all scheduled events
 */
public interface SystemScheduleEvent extends SystemEvent {

    String getGroup();

    String getKey();

}
