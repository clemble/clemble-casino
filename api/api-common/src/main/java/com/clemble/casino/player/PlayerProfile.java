package com.clemble.casino.player;

import org.springframework.data.annotation.Id;

import com.clemble.casino.CountryAware;
import com.clemble.casino.VersionAware;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract public class PlayerProfile implements PlayerAware, CountryAware, VersionAware {

    /**
     * Generated 25/01/13
     */
    private static final long serialVersionUID = -7544343898430552989L;

    @Id
    @JsonProperty(PlayerAware.JSON_ID)
    private String player;

    @JsonProperty("country")
    private String country;

    @Override
    public String getPlayer() {
        return player;
    }

    public PlayerProfile setPlayer(String player) {
        this.player = player;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((player == null) ? 0 : player.hashCode());
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
        PlayerProfile other = (PlayerProfile) obj;
        if (player == null) {
            if (other.player != null)
                return false;
        } else if (!player.equals(other.player))
            return false;
        return true;
    }

}
