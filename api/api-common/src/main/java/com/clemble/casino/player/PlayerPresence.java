package com.clemble.casino.player;

import java.util.ArrayList;
import java.util.Collection;

import com.clemble.casino.event.Event;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameSessionAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("presence")
public class PlayerPresence implements Event, PlayerAware, GameSessionAware, PresenceAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = 2110453101269621164L;

    final private String playerId;
    final private Presence presence;
    final private GameSessionKey session;

    @JsonCreator
    public PlayerPresence(@JsonProperty(PlayerAware.JSON_ID) String player, @JsonProperty("session") GameSessionKey session, @JsonProperty("presence") Presence presence) {
        this.playerId = player;
        this.session = session;
        this.presence = presence;
    }

    @Override
    public String getPlayer() {
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

    public static PlayerPresence offline(String player) {
        return new PlayerPresence(player, GameSessionAware.DEFAULT_SESSION, Presence.offline);
    }

    public static PlayerPresence online(String player) {
        return new PlayerPresence(player, GameSessionAware.DEFAULT_SESSION, Presence.online);
    }

    public static PlayerPresence playing(String player, GameSessionKey session) {
        return new PlayerPresence(player, session, Presence.playing);
    }

    public static PlayerPresence create(String player, Presence presence) {
        return new PlayerPresence(player, GameSessionAware.DEFAULT_SESSION, presence);
    }

    public static Collection<PlayerPresence> playing(Collection<String> players, GameSessionKey session) {
        Collection<PlayerPresence> playerPresences = new ArrayList<>();
        for (String player : players)
            playerPresences.add(playing(player, session));
        return playerPresences;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((playerId == null) ? 0 : playerId.hashCode());
        result = prime * result + ((presence == null) ? 0 : presence.hashCode());
        result = prime * result + ((session == null) ? 0 : session.hashCode());
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
        PlayerPresence other = (PlayerPresence) obj;
        if (playerId == null) {
            if (other.playerId != null)
                return false;
        } else if (!playerId.equals(other.playerId))
            return false;
        if (presence != other.presence)
            return false;
        if (session == null) {
            if (other.session != null)
                return false;
        } else if (!session.equals(other.session))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return playerId + ":" + presence + ":" + session;
    }

}