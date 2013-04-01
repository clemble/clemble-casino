package com.gogomaya.server.integration.player;

import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.wallet.PlayerWallet;

public class Player implements PlayerAware<Player> {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    private long playerId;

    private PlayerProfile profile;

    private PlayerWallet wallet;

    private PlayerIdentity identity;

    private PlayerCredential credential;

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
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
        return wallet;
    }

    public Player setWallet(PlayerWallet wallet) {
        this.wallet = wallet;
        return this;
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
