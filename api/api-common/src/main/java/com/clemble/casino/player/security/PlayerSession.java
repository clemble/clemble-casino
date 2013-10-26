package com.clemble.casino.player.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.player.PlayerAware;

@Entity
@Table(name = "PLAYER_SESSION")
public class PlayerSession implements PlayerAware {

    /**
     * 
     */
    private static final long serialVersionUID = 5003851677194227089L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    private long sessionId;

    @Column(name = "PLAYER_ID")
    private String player;

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

    public String getPlayer() {
        return player;
    }

    public PlayerSession setPlayer(String playerId) {
        this.player = playerId;
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
        result = prime * result + (int) (player == null ? 0 : player.hashCode());
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
        if (sessionId != other.sessionId)
            return false;
        return player.equals(other.player);
    }

    @Override
    public String toString() {
        return "PlayerSession [sessionId=" + sessionId + ", playerId=" + player + ", startTime=" + startTime + ", expirationTime=" + expirationTime + "]";
    }

}
