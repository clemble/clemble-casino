package com.gogomaya.server.player.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.server.game.ConstructionAware;
import com.gogomaya.server.player.PlayerAware;

@Entity
@Table(name = "PLAYER_SCHEDULED_EVENT")
public class PlayerScheduleEvent implements PlayerAware, ConstructionAware, Comparable<PlayerScheduleEvent> {

    /**
     * Generated 19/06/13
     */
    private static final long serialVersionUID = -361237726667152884L;

    @Id
    @Column(name = "PLAYER_ID")
    private long playerId;
    @Id
    @Column(name = "CONSTRUCTION_ID")
    private long construction;
    @Column(name = "START_TIME")
    private long startTime;
    @Column(name = "END_TIME")
    private long endTime;

    public PlayerScheduleEvent() {
    }

    @JsonCreator
    public PlayerScheduleEvent(@JsonProperty("playerId") long player,
            @JsonProperty("construction") long construction,
            @JsonProperty("startTime") long startTime,
            @JsonProperty("endTime") long endTime) {
        this.playerId = player;
        this.construction = construction;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public long getConstruction() {
        return construction;
    }

    public void setConstruction(long construction) {
        this.construction = construction;
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
        result = prime * result + (int) (construction ^ (construction >>> 32));
        result = prime * result + (int) (endTime ^ (endTime >>> 32));
        result = prime * result + (int) (getPlayerId() ^ (getPlayerId() >>> 32));
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
        if (construction != other.construction)
            return false;
        return (endTime == other.endTime) && (getPlayerId() == other.getPlayerId()) && (startTime == other.startTime);
    }
}
