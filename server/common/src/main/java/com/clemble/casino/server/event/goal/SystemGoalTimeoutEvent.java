package com.clemble.casino.server.event.goal;

import com.clemble.casino.server.event.SystemEvent;

/**
 * Created by mavarazy on 11/8/14.
 */
public class SystemGoalTimeoutEvent implements SystemGoalEvent {

    final public static String CHANNEL = "sys:goal:timeout";

    final private String goalKey;

    public SystemGoalTimeoutEvent(String goalKey) {
        this.goalKey = goalKey;
    }

    @Override
    public String getGoalKey() {
        return goalKey;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemGoalTimeoutEvent that = (SystemGoalTimeoutEvent) o;

        if (!goalKey.equals(that.goalKey)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return goalKey.hashCode();
    }

    @Override
    public String toString() {
        return goalKey + " > " + CHANNEL;
    }

}
