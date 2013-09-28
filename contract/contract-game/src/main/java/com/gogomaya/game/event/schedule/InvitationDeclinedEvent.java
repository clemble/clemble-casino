package com.gogomaya.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.player.PlayerAware;

@JsonTypeName("declined")
public class InvitationDeclinedEvent implements InvitationResponseEvent {

    /**
     * Generated 02/06/13
     */
    private static final long serialVersionUID = 655381424177654890L;

    final private String player;
    final private GameSessionKey session;

    @JsonCreator
    public InvitationDeclinedEvent(@JsonProperty(PlayerAware.JSON_ID) String player, @JsonProperty("session") GameSessionKey session) {
        this.player = player;
        this.session = session;
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (player == null ? 0 : player.hashCode());
        result = prime * result + (int) (session.hashCode());
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
        InvitationDeclinedEvent other = (InvitationDeclinedEvent) obj;
        if (player != other.player)
            return false;
        if (session != other.session)
            return false;
        return true;
    }

}
