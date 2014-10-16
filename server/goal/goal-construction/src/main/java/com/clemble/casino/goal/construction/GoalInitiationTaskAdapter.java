package com.clemble.casino.goal.construction;

import com.clemble.casino.event.Event;
import com.clemble.casino.server.event.SystemEvent;
import com.clemble.casino.server.executor.EventTask;
import com.clemble.casino.server.executor.EventTaskAdapter;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Collection;

/**
 * Created by mavarazy on 9/13/14.
 */
public class GoalInitiationTaskAdapter implements EventTaskAdapter {

    final private SystemNotificationService notificationService;

    public GoalInitiationTaskAdapter(SystemNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void process(EventTask task) {
        // Step 1. Processing task
        Collection<? extends Event> events = task.execute();
        // Step 1.1 If there is no events nothing much to do return
        if(events.isEmpty())
            return;
        if(task instanceof GoalInitiationExpirationTask) {
            // Step 2.2 Processing initiation expiration
            events.forEach(event -> notificationService.send((SystemEvent) event));
        } else {
            throw new IllegalAccessError();
        }
    }

}
