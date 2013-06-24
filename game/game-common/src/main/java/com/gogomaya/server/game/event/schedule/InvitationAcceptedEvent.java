package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ScheduleEvent;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("accepted")
public class InvitationAcceptedEvent implements ScheduleEvent, PlayerAware {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = -4465974655141746411L;

    private long playerId;

    private long session;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public long getSession() {
        return session;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    @Override
    public int hashCode() {
        return 31 * ( 31 + (int) (playerId ^ (playerId >>> 32))) + (int) (session ^ (session >>> 32));
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
        if (playerId != other.playerId)
            return false;
        if (session != other.session)
            return false;
        return true;
    }

}
