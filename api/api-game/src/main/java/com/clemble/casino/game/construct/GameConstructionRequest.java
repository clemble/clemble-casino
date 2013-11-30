package com.clemble.casino.game.construct;

import java.io.Serializable;

import com.clemble.casino.game.specification.GameSpecification;
import com.clemble.casino.game.specification.GameSpecificationAware;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract public class GameConstructionRequest implements GameSpecificationAware, Serializable {

    /**
     * Generated 28/11/13
     */
    private static final long serialVersionUID = -7124151122750295287L;

    final private GameSpecification specification;

    public GameConstructionRequest(GameSpecification specification) {
        this.specification = specification;
    }

    @Override
    public GameSpecification getSpecification() {
        return specification;
    }

    @Override
    public int hashCode() {
        return ((specification == null) ? 0 : specification.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GameConstructionRequest other = (GameConstructionRequest) obj;
        if (specification == null) {
            if (other.specification != null)
                return false;
        } else if (!specification.equals(other.specification))
            return false;
        return true;
    }

}
