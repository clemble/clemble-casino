package com.clemble.casino.player.security;

import javax.crypto.SecretKey;

import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.client.RSAKeySecretFormat.KeySerializer;
import com.clemble.casino.player.client.RSAKeySecretFormat.SecretKeyDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PlayerToken implements PlayerAware {

    /**
     * Generated 15/02/13
     */
    private static final long serialVersionUID = 7757779068859351877L;

    final private String player;
    final private String consumerKey;
    final private SecretKey secret;
    final private String value;

    @JsonCreator
    public PlayerToken(@JsonProperty(PlayerAware.JSON_ID) String player,
            @JsonSerialize(using = KeySerializer.class)
            @JsonDeserialize(using = SecretKeyDeserializer.class)
            @JsonProperty("secretKey") SecretKey secret,
            @JsonProperty("consumerKey") String consumerKey,
            @JsonProperty("value") String value) {
        this.player = player;
        this.secret = secret;
        this.consumerKey = consumerKey;
        this.value = value;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public SecretKey getSecretKey() {
        return secret;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getValue() {
        return value;
    }

}
