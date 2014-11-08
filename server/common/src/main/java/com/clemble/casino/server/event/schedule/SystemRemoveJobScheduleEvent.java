package com.clemble.casino.server.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by mavarazy on 11/8/14.
 */
public class SystemRemoveJobScheduleEvent implements SystemScheduleEvent {

    final public static String CHANNEL = "sys:schedule:remove:job";

    final private String group;
    final private String key;

    @JsonCreator
    public SystemRemoveJobScheduleEvent(@JsonProperty("group") String group, @JsonProperty("key") String key) {
        this.group = group;
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getChannel() {
        return CHANNEL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SystemRemoveJobScheduleEvent that = (SystemRemoveJobScheduleEvent) o;

        if (!group.equals(that.group)) return false;
        if (!key.equals(that.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = group.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getGroup() + ":" + getKey() + " > " + CHANNEL;
    }

}
