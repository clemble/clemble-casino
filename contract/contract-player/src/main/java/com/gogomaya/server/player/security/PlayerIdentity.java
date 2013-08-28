package com.gogomaya.server.player.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gogomaya.player.PlayerAware;

@Entity
@Table(name = "PLAYER_IDENTITY")
public class PlayerIdentity implements PlayerAware {

    /**
     * Generated 15/02/13
     */
    private static final long serialVersionUID = 7757779068859351877L;

    @Id
    @Column(name = "PLAYER_ID")
    @JsonProperty("playerId")
    private long profileId;

    @Column(name = "SECRET")
    private String secret;

    @Override
    public long getPlayerId() {
        return profileId;
    }

    public PlayerIdentity setPlayerId(long profileId) {
        this.profileId = profileId;
        return this;
    }

    public String getSecret() {
        return secret;
    }

    public PlayerIdentity setSecret(String secret) {
        this.secret = secret;
        return this;
    }
}
