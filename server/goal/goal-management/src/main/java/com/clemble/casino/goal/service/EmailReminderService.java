package com.clemble.casino.goal.service;

import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Date;

/**
 * Created by mavarazy on 12/12/14.
 */
public class EmailReminderService {

    final private SystemNotificationService notificationService;

    public EmailReminderService(SystemNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void scheduleReminder(String player, String goalKey, String text, Date breachTime) {
        String key = toKey(player);
        // Step 1. Cancel reminder
        notificationService.send(new SystemRemoveJobScheduleEvent(goalKey, key));
        // Step 2. Schedule notification for a new breach time
        // Step 2.1 Generate email notification
        SystemEmailSendRequestEvent emailRequest = new SystemEmailSendRequestEvent(player, text);
        // Step 2.2 Schedule email notification
        notificationService.send(new SystemAddJobScheduleEvent(goalKey, key, emailRequest, breachTime));
    }

    private String toKey(String player) {
        return "reminder:email:" + player;
    }

}
