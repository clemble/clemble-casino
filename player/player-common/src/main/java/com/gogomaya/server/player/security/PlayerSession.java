package com.gogomaya.server.player.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PLAYER_SESSION")
public class PlayerSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SESSION_ID")
    private long sessionId;

    @Column(name = "PLAYER_ID")
    private long playerId;

    @Column(name = "SERVER")
    private String server;

    @Column(name = "START_TIME")
    private Date startTime = new Date();

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

    public String getServer() {
        return server;
    }

    public PlayerSession setServer(String server) {
        this.server = server;
        return this;
    }

    public Date getStartTime() {
        return startTime;
    }

    public PlayerSession setStartTime(Date startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        result = prime * result + ((server == null) ? 0 : server.hashCode());
        result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
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
        if (server == null) {
            if (other.server != null)
                return false;
        } else if (!server.equals(other.server))
            return false;
        if (sessionId != other.sessionId)
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        return true;
    }

}
