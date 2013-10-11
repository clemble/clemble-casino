package com.clemble.casino.server.player.schedule;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.SessionAware;
import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "PLAYER_SCHEDULED_EVENT")
public class PlayerScheduleEvent implements PlayerAware, SessionAware, Comparable<PlayerScheduleEvent> {

    /**
     * Generated 19/06/13
     */
    private static final long serialVersionUID = -361237726667152884L;

    @Id
    @Column(name = "PLAYER_ID")
    private String player;
    @Embedded
    private GameSessionKey session;
    @Column(name = "START_TIME")
    private long startTime;
    @Column(name = "END_TIME")
    private long endTime;

    public PlayerScheduleEvent() {
    }

    @JsonCreator
    public PlayerScheduleEvent(@JsonProperty(PlayerAware.JSON_ID) String player,
            @JsonProperty("session") GameSessionKey construction,
            @JsonProperty("startTime") long startTime,
            @JsonProperty("endTime") long endTime) {
        this.player = player;
        this.session = construction;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public PlayerScheduleEvent setPlayer(String player) {
        this.player = player;
        return this;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public void setSession(GameSessionKey session) {
        this.session = session;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int compareTo(PlayerScheduleEvent o) {
        return Long.compare(o.getStartTime(), getStartTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (session != null ? session.hashCode() : 0);
        result = prime * result + (int) (endTime ^ (endTime >>> 32));
        result = prime * result + (int) (player == null ? 0 : player.hashCode());
        result = prime * result + (int) (startTime ^ (startTime >>> 32));
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
        if (session != other.session)
            return false;
        return (endTime == other.endTime) && (getPlayer() == other.getPlayer()) && (startTime == other.startTime);
    }
}
