package com.clemble.casino.server.game.action;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.lifecycle.management.event.GameManagementEvent;
import com.clemble.casino.server.action.ClembleManager;
import com.clemble.casino.server.aspect.time.PlayerTimeTask;
import com.clemble.casino.server.executor.EventTask;
import com.clemble.casino.server.executor.EventTaskAdapter;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Collection;

/**
 * Created by mavarazy on 9/9/14.
 */
public class GameEventTaskAdapter implements EventTaskAdapter {

    final private GameManagerFactory managerFactory;
    final private SystemNotificationService notificationService;

    public GameEventTaskAdapter(GameManagerFactory managerFactory, SystemNotificationService notificationService) {
        this.managerFactory = managerFactory;
        this.notificationService = notificationService;
    }

    @Override
    public void process(EventTask task) {
        // Step 1. Processing task
        Collection<? extends Event> events = task.execute();
        // Step 1.1 If there is no events nothing much to do return
        if(events.isEmpty())
            return;
        if(task instanceof PlayerTimeTask) {
            // Step 2.1 Fetching session key
            String sessionKey = task.getKey();
            // Step 2.1.1 Fetching manager
            ClembleManager<GameManagementEvent, ?> manager = this.managerFactory.get(sessionKey);
            // Step 2.1.2 Going event by event and processing
            events.forEach(event -> manager.process(event));
        } else {
            throw new IllegalAccessError();
        }
    }


}
