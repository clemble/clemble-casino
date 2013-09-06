package com.gogomaya.player.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gogomaya.configuration.ResourceLocations;

@Entity
@Table(name = "PLAYER_SESSION")
public class PlayerSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    private long sessionId;

    @Column(name = "PLAYER_ID")
    private long playerId;

    @Column(name = "START_TIME")
    private Date startTime = new Date();

    @Column(name = "EXPIRATION_TIME")
    private Date expirationTime = new Date();

    @Transient
    private ResourceLocations resourceLocations;

    public long getSessionId() {
        return sessionId;
    }

    public PlayerSession setSessionId(long sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public long getPlayerId() {
        return playerId;
    }

    public PlayerSession setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    public ResourceLocations getResourceLocations() {
        return resourceLocations;
    }

    public void setResourceLocations(ResourceLocations resourceLocations) {
        this.resourceLocations = resourceLocations;
    }

    public Date getStartTime() {
        return startTime;
    }

    public PlayerSession setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean expired() {
        return System.currentTimeMillis() >= expirationTime.getTime();
    }

    public void markExpired() {
        this.expirationTime = new Date();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
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
        PlayerSession other = (PlayerSession) obj;
        if (playerId != other.playerId)
            return false;
        if (sessionId != other.sessionId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PlayerSession [sessionId=" + sessionId + ", playerId=" + playerId + ", startTime=" + startTime + ", expirationTime=" + expirationTime + "]";
    }

}
