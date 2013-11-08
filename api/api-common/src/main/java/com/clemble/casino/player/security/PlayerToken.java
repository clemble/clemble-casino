package com.clemble.casino.player.security;

import com.clemble.casino.player.PlayerAware;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerToken implements PlayerAware {

    /**
     * Generated 15/02/13
     */
    private static final long serialVersionUID = 7757779068859351877L;

    final private String player;
    final private String secret;
    final private String value;

    @JsonCreator
    public PlayerToken(@JsonProperty(PlayerAware.JSON_ID) String player, @JsonProperty("secret") String secret, @JsonProperty("value") String value) {
        this.player = player;
        this.secret = secret;
        this.value = value;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public String getSecret() {
        return secret;
    }

    public String getValue() {
        return value;
    }

}
