package com.gogomaya.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("accepted")
public class InvitationAcceptedEvent implements InvitationResponseEvent {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = -4465974655141746411L;

    final private String player;
    final private GameSessionKey session;

    @JsonCreator
    public InvitationAcceptedEvent(@JsonProperty("session") GameSessionKey session, @JsonProperty(PlayerAware.JSON_ID) String player) {
        this.session = session;
        this.player = player;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public GameSessionKey getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        return player.hashCode() + session.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        InvitationAcceptedEvent other = (InvitationAcceptedEvent) obj;
        if (player != other.player)
            return false;
        return session.equals(other.session);
    }

}
