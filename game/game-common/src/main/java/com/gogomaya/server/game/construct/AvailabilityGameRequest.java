package com.gogomaya.server.game.construct;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("availability")
public class AvailabilityGameRequest extends GameRequest {

    /**
     * Generated 12/06/13
     */
    private static final long serialVersionUID = -3051736949418145655L;

    private Collection<Long> opponents;

    public Collection<Long> getOpponents() {
        return opponents;
    }

    public void setOpponents(Collection<Long> opponents) {
        this.opponents = opponents;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((opponents == null) ? 0 : opponents.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        AvailabilityGameRequest other = (AvailabilityGameRequest) obj;
        if (opponents == null) {
            if (other.opponents != null)
                return false;
        } else if (!opponents.equals(other.opponents))
            return false;
        return true;
    }
}
