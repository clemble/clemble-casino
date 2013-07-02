package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Closeable;

import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.integration.game.GameSessionListener;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenersManager;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.player.wallet.PlayerWallet;

public class Player implements PlayerAware, Closeable {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private long playerId;
    final private PlayerSession session;
    final private PlayerIdentity identity;
    final private PlayerListenersManager playerListenersManager;
    final private PlayerOperations playerOperations;

    private PlayerProfile profile;

    private PlayerCredential credential;

    public Player(final PlayerIdentity playerIdentity, final PlayerOperations playerOperations, final PlayerListenerOperations listenerOperations) {
        this.playerOperations = checkNotNull(playerOperations);
        this.identity = checkNotNull(playerIdentity);
        this.playerId = identity.getPlayerId();
        this.session = checkNotNull(playerOperations.startSession(playerIdentity));
        this.playerListenersManager = new PlayerListenersManager(this, listenerOperations);
    }

    @Override
    public long getPlayerId() {
        return playerId;
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

    public PlayerCredential getCredential() {
        return credential;
    }

    public Player setCredential(PlayerCredential credential) {
        this.credential = credential;
        return this;
    }

    public PlayerSession getSession() {
        return session;
    }

    public void listen(long session, GameSessionListener sessionListener) {
        playerListenersManager.listen(session, sessionListener);
    }

    public void listen(GameConstruction construction, GameSessionListener sessionListener) {
        playerListenersManager.listen(construction, sessionListener);
    }

    @Override
    public void close() {
        playerListenersManager.close();
    }

}
