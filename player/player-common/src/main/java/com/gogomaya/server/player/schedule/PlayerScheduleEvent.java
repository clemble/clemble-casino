package com.gogomaya.server.player.schedule;

import static com.google.common.base.Preconditions.checkNotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ScheduleEvent;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("schedule")
public class PlayerScheduleEvent implements ScheduleEvent, PlayerAware, Comparable<PlayerScheduleEvent> {

    /**
     * Generated 19/06/13
     */
    private static final long serialVersionUID = -361237726667152884L;

    final long playerId;
    final long sessionId;
    final private long scheduledTime;
    final private ScheduledEventPriority priority;

    public PlayerScheduleEvent(long session, long player) {
        this(session, player, System.currentTimeMillis());
    }

    public PlayerScheduleEvent(long session, long player, long scheduledDate) {
        this(session, player, scheduledDate, ScheduledEventPriority.low);
    }

    @JsonCreator
    public PlayerScheduleEvent(@JsonProperty("session") long session, @JsonProperty("playerId") long player, @JsonProperty("scheduledTime") long scheduledTime,
            @JsonProperty("priority") ScheduledEventPriority eventPriority) {
        this.playerId = player;
        this.sessionId = session;
        this.scheduledTime = scheduledTime;
        this.priority = checkNotNull(eventPriority);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public long getSession() {
        return sessionId;
    }

    public ScheduledEventPriority getPriority() {
        return priority;
    }

    public long getScheduledTime() {
        return scheduledTime;
    }

    @Override
    public int compareTo(PlayerScheduleEvent o) {
        int comparison = Long.compare(o.scheduledTime, scheduledTime);
        return comparison == 0 ? o.priority.compareTo(priority) : comparison;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result + (int) (scheduledTime ^ (scheduledTime >>> 32));
        result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerScheduleEvent other = (PlayerScheduleEvent) obj;
        if (playerId != other.playerId)
            return false;
        if (priority != other.priority)
            return false;
        if (scheduledTime != other.scheduledTime)
            return false;
        if (sessionId != other.sessionId)
            return false;
        return true;
    }

}
