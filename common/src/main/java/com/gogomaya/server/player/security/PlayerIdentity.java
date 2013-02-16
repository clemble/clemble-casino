package com.gogomaya.server.player.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.cloudfoundry.org.codehaus.jackson.annotate.JsonProperty;

import com.gogomaya.server.player.PlayerAware;

@Entity
@Table(name = "USER_IDENTITY")
public class PlayerIdentity implements PlayerAware<PlayerIdentity>, Serializable {

    /**
     * Generated 15/02/13
     */
    private static final long serialVersionUID = 7757779068859351877L;

    @Id
    @Column(name = "PROFILE_ID")
    @JsonProperty("profileId")
    private long profileId;

    @Column(name = "SECRET")
    private String secret;

    @Override
    public long getPlayerId() {
        return profileId;
    }

    @Override
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
