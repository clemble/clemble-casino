package com.clemble.casino.goal.service;

import com.clemble.casino.server.event.email.SystemEmailSendRequestEvent;
import com.clemble.casino.server.event.phone.SystemPhoneSMSSendRequestEvent;
import com.clemble.casino.server.event.schedule.SystemAddJobScheduleEvent;
import com.clemble.casino.server.event.schedule.SystemRemoveJobScheduleEvent;
import com.clemble.casino.server.player.notification.SystemNotificationService;

import java.util.Date;

/**
 * Created by mavarazy on 12/12/14.
 */
public class PhoneReminderService implements ReminderService {

    final private SystemNotificationService notificationService;

    public PhoneReminderService(SystemNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void scheduleReminder(String player, String goalKey, String text, Date breachTime) {
        String key = toKey(player);
        // Step 1. Cancel reminder
        notificationService.send(new SystemRemoveJobScheduleEvent(goalKey, key));
        // Step 2. Schedule notification for a new breach time
        // Step 2.1 Generate email notification
        SystemPhoneSMSSendRequestEvent smsRequest = new SystemPhoneSMSSendRequestEvent(player, text);
        // Step 2.2 Schedule email notification
        notificationService.send(new SystemAddJobScheduleEvent(goalKey, key, smsRequest, breachTime));
    }

    @Override
    public void cancelReminder(String player, String goalKey) {
        String key = toKey(player);
        // Step 1. Cancel reminder
        notificationService.send(new SystemRemoveJobScheduleEvent(goalKey, key));
    }

    private String toKey(String player) {
        return "reminder:phone:" + player;
    }

}
