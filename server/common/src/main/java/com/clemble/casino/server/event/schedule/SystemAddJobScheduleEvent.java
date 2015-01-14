package com.clemble.casino.server.event.schedule;

import com.clemble.casino.server.event.SystemEvent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by mavarazy on 11/8/14.
 */
public class SystemAddJobScheduleEvent implements SystemScheduleEvent {

    final public static String CHANNEL = "sys:schedule:add:job";

    final private String group;
    final private String key;
    final private SystemEvent event;
    final private DateTime triggerStartTime;

    @JsonCreator
    public SystemAddJobScheduleEvent(@JsonProperty("group") String group, @JsonProperty("key") String key, @JsonProperty("event") SystemEvent event, @JsonProperty("triggerStartTime") DateTime triggerStartTime) {
        this.group = group;
        this.key = key;
        this.triggerStartTime = triggerStartTime;
        this.event = event;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public DateTime getTriggerStartTime() {
        return triggerStartTime;
    }

    public SystemEvent getEvent() {
        return event;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemAddJobScheduleEvent that = (SystemAddJobScheduleEvent) o;

        if (event != null ? !event.equals(that.event) : that.event != null) return false;
        if (group != null ? !group.equals(that.group) : that.group != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (triggerStartTime != null ? !triggerStartTime.equals(that.triggerStartTime) : that.triggerStartTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = group != null ? group.hashCode() : 0;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (event != null ? event.hashCode() : 0);
        result = 31 * result + (triggerStartTime != null ? triggerStartTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getGroup() + ":" + getKey() + " > " + CHANNEL + " > " + event;
    }

}
