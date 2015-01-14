package com.clemble.casino.goal.service;

import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Map;

/**
 * Created by mavarazy on 12/12/14.
 */
public class EmailReminderService implements ReminderService {

    final private SystemNotificationService notificationService;

    public EmailReminderService(SystemNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void scheduleReminder(String player, String goalKey, String template, Map<String, String> parameters, DateTime breachTime) {
        String key = toKey(player);
        // Step 1. Cancel reminder
        // WARNING Since this is asynchronous, this might cause problems, it's better to do it in reminder service cancelReminder(player, goalKey);
        // Step 2. Schedule notification for a new breach time
        // Step 2.1 Generate email notification
        SystemEmailSendRequestEvent emailRequest = new SystemEmailSendRequestEvent(player, template, parameters);
        // Step 2.2 Schedule email notification
        notificationService.send(new SystemAddJobScheduleEvent(goalKey, key, emailRequest, breachTime));
    }

    @Override
    public void cancelReminder(String player, String goalKey) {
        String key = toKey(player);
        // Step 1. Cancel reminder
        notificationService.send(new SystemRemoveJobScheduleEvent(goalKey, key));
    }

    private String toKey(String player) {
        return "reminder:email:" + player;
    }

}
