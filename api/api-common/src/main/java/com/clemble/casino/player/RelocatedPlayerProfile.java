package com.clemble.casino.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("relocated")
public class RelocatedPlayerProfile extends PlayerProfile {

    /**
     * Generated 04/11/13
     */
    private static final long serialVersionUID = -7122319584839524952L;

    private String newLocation;

    public String getNewLocation() {
        return newLocation;
    }

    public void setNewLocation(String newPlayerProfile) {
        this.newLocation = newPlayerProfile;
    }

    @Override
    @JsonIgnore
    public int getVersion() {
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((newLocation == null) ? 0 : newLocation.hashCode());
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
        RelocatedPlayerProfile other = (RelocatedPlayerProfile) obj;
        if (newLocation == null) {
            if (other.newLocation != null)
                return false;
        } else if (!newLocation.equals(other.newLocation))
            return false;
        return true;
    }

}
