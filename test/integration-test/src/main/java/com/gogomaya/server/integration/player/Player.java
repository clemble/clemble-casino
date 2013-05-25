package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.wallet.PlayerWallet;

public class Player implements PlayerAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    private long playerId;

    private PlayerOperations playerOperations;

    private PlayerProfile profile;

    private PlayerIdentity identity;

    private PlayerCredential credential;

    public Player(PlayerOperations playerOperations) {
        this.playerOperations = checkNotNull(playerOperations);
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public Player setPlayerId(long playerId) {
        this.playerId = playerId;
        return this;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public Player setProfile(PlayerProfile profile) {
        this.profile = profile;
        this.profile.setPlayerId(playerId);
        return this;
    }

    public PlayerWallet getWallet() {
        return playerOperations.wallet(this, playerId);
    }

    public PlayerIdentity getIdentity() {
        return identity;
    }

    public Player setIdentity(PlayerIdentity identity) {
        this.identity = identity;
        return this;
    }

    public PlayerCredential getCredential() {
        return credential;
    }

    public Player setCredential(PlayerCredential credential) {
        this.credential = credential;
        return this;
    }

}
