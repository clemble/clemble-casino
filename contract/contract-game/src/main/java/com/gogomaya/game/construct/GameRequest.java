package com.gogomaya.game.construct;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gogomaya.game.specification.GameSpecification;
import com.gogomaya.game.specification.GameSpecificationAware;
import com.gogomaya.player.PlayerAware;
import com.gogomaya.utils.CollectionUtils;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract public class GameRequest implements PlayerAware, GameSpecificationAware, GameOpponentsAware {

    /**
     * Generated 10/06/13
     */
    private static final long serialVersionUID = 4949060894194971610L;

    final private String player;
    final private Collection<String> participants;

    final private GameSpecification specification;

    public GameRequest(String player, GameSpecification specification) {
        this(player, specification, CollectionUtils.immutableSet(player));
    }

    public GameRequest(String player, GameSpecification specification, Collection<String> participants) {
        this.player = player;
        this.specification = specification;

        if (!participants.contains(player))
            participants.add(player);
        this.participants = participants;
    }

    @Override
    public Collection<String> getParticipants() {
        return participants;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (player != null ? player.hashCode() : 0);
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
        if (player != other.player)
            return false;
        if (specification == null) {
            if (other.specification != null)
                return false;
        } else if (!specification.equals(other.specification))
            return false;
        return true;
    }

}
