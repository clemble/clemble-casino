package com.clemble.casino.player.web;

import java.io.Serializable;

import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerLoginRequest implements Serializable {

    private static final long serialVersionUID = -5259569180816587376L;

    final private PlayerCredential playerCredential;
    final private ClembleConsumerDetails consumerDetails;

    @JsonCreator
    public PlayerLoginRequest(@JsonProperty("consumerDetails") final ClembleConsumerDetails consumerDetails,
            @JsonProperty("playerCredential") final PlayerCredential playerCredential) {
        this.playerCredential = playerCredential;
        this.consumerDetails = consumerDetails;
    }

    final public PlayerCredential getPlayerCredential() {
        return playerCredential;
    }

    public ClembleConsumerDetails getConsumerDetails() {
        return consumerDetails;
    }

}
