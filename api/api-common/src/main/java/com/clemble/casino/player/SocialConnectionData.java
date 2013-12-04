package com.clemble.casino.player;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.clemble.casino.error.ClembleCasinoError.Code;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Minimum social ConnectionData, that is sufficient to create a user profile in internal network.
 * 
 * @author Anton O
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SocialConnectionData implements Serializable {

    /**
     * Generated 25/01/2013
     */
    private static final long serialVersionUID = -6236465246404418965L;

    @JsonProperty("providerId")
    @NotNull(message = Code.SOCIAL_CONNECTION_PROVIDER_ID_NULL_CODE)
    final private String providerId;

    @JsonProperty("providerUserId")
    @NotNull(message = Code.SOCIAL_CONNECTION_PROVIDER_USER_NULL_CODE)
    final private String providerUserId;

    @JsonProperty("accessToken")
    final private String accessToken;

    @JsonProperty("secret")
    final private String secret;

    @JsonProperty("refreshToken")
    final private String refreshToken;

    @JsonProperty("expireTime")
    final private long expireTime;

    @JsonCreator
    public SocialConnectionData(@JsonProperty("providerId") final String providerId,
            @JsonProperty("providerUserId") final String providerUserId,
            @JsonProperty("accessToken") final String accessToken,
            @JsonProperty("secret") final String secret,
            @JsonProperty("refreshToken") final String refreshToken,
            @JsonProperty("expireTime") final long expireTime) {
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    /**
     * The id of the provider the connection is associated with.
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * The id of the provider user this connection is connected to.
     */
    public String getProviderUserId() {
        return providerUserId;
    }

    /**
     * The access token required to make authorized API calls.
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * The secret token needed to make authorized API calls.
     * Required for OAuth1-based connections.
     */
    public String getSecret() {
        return secret;
    }

    /**
     * A token use to renew this connection. Optional.
     * Always null for OAuth1-based connections.
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * The time the connection expires. Optional.
     * Always null for OAuth1-based connections.
     */
    public long getExpireTime() {
        return expireTime;
    }

}
