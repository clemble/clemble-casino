package com.clemble.casino.goal.service;

import java.util.Date;

/**
 * Created by mavarazy on 12/12/14.
 */
public interface ReminderService  {

    public void scheduleReminder(String player, String goalKey, String text, Date breachTime);

    public void cancelReminder(String player, String goalKey);

}
