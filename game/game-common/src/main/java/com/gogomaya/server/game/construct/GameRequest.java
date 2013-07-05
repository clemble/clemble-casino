package com.gogomaya.server.game.construct;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.server.game.specification.GameSpecification;
import com.gogomaya.server.game.specification.GameSpecificationAware;
import com.gogomaya.server.player.PlayerAware;
import com.google.common.collect.ImmutableSet;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract public class GameRequest implements PlayerAware, GameSpecificationAware, GameOpponentsAware {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 4949060894194971610L;

    final private long playerId;

    final private GameSpecification specification;

    final private Collection<Long> participants;

    public GameRequest(long playerId, GameSpecification specification) {
        this(playerId, specification, ImmutableSet.<Long> of(playerId));
    }

    public GameRequest(long playerId, GameSpecification specification, Collection<Long> participants) {
        this.playerId = playerId;
        this.specification = specification;

        if (!participants.contains(playerId))
            participants.add(playerId);
        this.participants = participants;
    }

    @Override
    public Collection<Long> getParticipants() {
        return participants;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (playerId ^ (playerId >>> 32));
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
        GameRequest other = (GameRequest) obj;
        if (playerId != other.playerId)
            return false;
        if (specification == null) {
            if (other.specification != null)
                return false;
        } else if (!specification.equals(other.specification))
            return false;
        return true;
    }

}
