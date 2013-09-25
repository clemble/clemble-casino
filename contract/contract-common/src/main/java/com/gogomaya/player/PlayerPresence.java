package com.gogomaya.player;

import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.event.Event;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.SessionAware;

public class PlayerPresence implements Event, PlayerAware, SessionAware, PresenceAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = 2110453101269621164L;

    final private long playerId;
    final private Presence presence;
    final private GameSessionKey session;

    @JsonCreator
    public PlayerPresence(@JsonProperty("playerId") long playerId, @JsonProperty("session") GameSessionKey session, @JsonProperty("presence") Presence presence) {
        this.playerId = playerId;
        this.session = session;
        this.presence = presence;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public Presence getPresence() {
        return presence;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    public static PlayerPresence offline(long playerId) {
        return new PlayerPresence(playerId, SessionAware.DEFAULT_SESSION, Presence.offline);
    }

    public static PlayerPresence online(long playerId) {
        return new PlayerPresence(playerId, SessionAware.DEFAULT_SESSION, Presence.online);
    }

    public static PlayerPresence playing(long playerId, GameSessionKey session) {
        return new PlayerPresence(playerId, session, Presence.playing);
    }

    public static PlayerPresence create(long playerId, Presence presence) {
        return new PlayerPresence(playerId, SessionAware.DEFAULT_SESSION, presence);
    }

    public static Collection<PlayerPresence> playing(Collection<Long> players, GameSessionKey session) {
        Collection<PlayerPresence> playerPresences = new ArrayList<>();
        for (Long player : players)
            playerPresences.add(playing(player, session));
        return playerPresences;
    }

}