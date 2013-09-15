package com.gogomaya.player;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.event.Event;
import com.gogomaya.game.SessionAware;

@Entity
@Table(name = "PLAYER_PRESENCE")
public class PlayerPresence implements Event, PlayerAware, SessionAware, PresenceAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = 2110453101269621164L;

    @Id
    @Column(name = "PLAYER_ID")
    private long playerId;

    @Column(name = "PRESENCE")
    private Presence presence;

    @Column(name = "SESSION_ID")
    private long sessionId;

    public PlayerPresence() {
    }

    @JsonCreator
    public PlayerPresence(@JsonProperty("playerId") long playerId, @JsonProperty("sessionId") long sessionId, @JsonProperty("presence") Presence presence) {
        this.playerId = playerId;
        this.sessionId = sessionId;
        this.presence = presence;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public Presence getPresence() {
        return presence;
    }

    public void setPresence(Presence presence) {
        this.presence = presence;
    }

    @Override
    public long getSession() {
        return sessionId;
    }

    public static PlayerPresence offline(long playerId) {
        return new PlayerPresence(playerId, SessionAware.DEFAULT_SESSION, Presence.offline);
    }

    public static PlayerPresence online(long playerId) {
        return new PlayerPresence(playerId, SessionAware.DEFAULT_SESSION, Presence.online);
    }

    public static PlayerPresence playing(long playerId, long sessionId) {
        return new PlayerPresence(playerId, sessionId, Presence.playing);
    }

    public static Collection<PlayerPresence> playing(Collection<Long> players, long session) {
        Collection<PlayerPresence> playerPresences = new ArrayList<>();
        for (Long player : players)
            playerPresences.add(playing(player, session));
        return playerPresences;
    }

}