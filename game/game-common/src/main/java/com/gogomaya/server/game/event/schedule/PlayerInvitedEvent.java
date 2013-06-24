package com.gogomaya.server.game.event.schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gogomaya.server.event.ScheduleEvent;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;
import com.gogomaya.server.player.PlayerAware;

@JsonTypeName("invited")
public class PlayerInvitedEvent implements ScheduleEvent, PlayerAware, GameSpecificationAware {

    /**
     * Generated 02/06/2013
     */
    private static final long serialVersionUID = 1753173974867187325L;

    final private long session;
    final private long playerId;
    final private GameSpecification specification;

    @JsonCreator
    public PlayerInvitedEvent(@JsonProperty("session") long session, @JsonProperty("playerId") long playerId,
            @JsonProperty("specification") GameSpecification specification) {
        this.session = session;
        this.playerId = playerId;
        this.specification = specification;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public long getSession() {
        return session;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
        result = prime * result + (int) (session ^ (session >>> 32));
        result = prime * result + ((specification == null) ? 0 : specification.hashCode());
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
        if (playerId != other.playerId)
            return false;
        if (session != other.session)
            return false;
        if (specification == null) {
            if (other.specification != null)
                return false;
        } else if (!specification.equals(other.specification))
            return false;
        return true;
    }

}
