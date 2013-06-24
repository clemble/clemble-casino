package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ScheduleEvent;
import com.gogomaya.server.game.SessionAware;
import com.gogomaya.server.game.construct.GameRequest;

@JsonTypeName("invited")
public class PlayerInvitedEvent implements ScheduleEvent, SessionAware {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = 1753173974867187325L;

    final private long session;

    final private GameRequest request;

    @JsonCreator
    public PlayerInvitedEvent(@JsonProperty("request") GameRequest request, @JsonProperty("session") long session) {
        this.session = session;
        this.request = request;
    }

    @Override
    public long getSession() {
        return session;
    }

    public GameRequest getRequest() {
        return request;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((request == null) ? 0 : request.hashCode());
        result = prime * result + (int) (session ^ (session >>> 32));
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
        PlayerInvitedEvent other = (PlayerInvitedEvent) obj;
        if (request == null) {
            if (other.request != null)
                return false;
        } else if (!request.equals(other.request))
            return false;
        if (session != other.session)
            return false;
        return true;
    }

}
