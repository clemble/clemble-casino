package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.game.construct.GameConstruction;
import com.gogomaya.server.integration.game.GameSessionListener;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.PlayerGameConstructionOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerManager;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.PlayerProfileOperations;
import com.gogomaya.server.integration.player.profile.ProfileOperations;
import com.gogomaya.server.integration.player.session.PlayerSessionOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.gogomaya.server.integration.player.wallet.PlayerWalletOperations;
import com.gogomaya.server.integration.player.wallet.WalletOperations;
import com.gogomaya.server.player.PlayerAware;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerSession;
import com.google.common.collect.ImmutableMap;

public class Player implements PlayerAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private long playerId;
    final private PlayerSession session;
    final private PlayerIdentity identity;
    final private PlayerListenerManager playerListenersManager;
    final private PlayerCredential credential;

    final private Map<String, PlayerGameConstructionOperations<?>> gameConstructors;

    final private PlayerSessionOperations playerSessionOperations;
    final private PlayerWalletOperations playerWalletOperations;
    final private PlayerProfileOperations profileOperations;

    public Player(final PlayerIdentity playerIdentity,
            final PlayerCredential credential,
            final ProfileOperations playerProfileOperations,
            final SessionOperations sessionOperations,
            final WalletOperations walletOperations,
            final PlayerListenerOperations listenerOperations,
            final GameConstructionOperations<?>... playerConstructionOperations) {
        this.profileOperations = new PlayerProfileOperations(this, playerProfileOperations);
        this.playerSessionOperations = new PlayerSessionOperations(this, sessionOperations);
        this.playerWalletOperations = new PlayerWalletOperations(this, walletOperations);

        this.identity = checkNotNull(playerIdentity);
        this.playerId = identity.getPlayerId();
        this.session = checkNotNull(playerSessionOperations.start());
        this.credential = checkNotNull(credential);
        this.playerListenersManager = new PlayerListenerManager(this, listenerOperations);

        Map<String, PlayerGameConstructionOperations<?>> map = new HashMap<>();
        for (GameConstructionOperations<?> constructionOperation : playerConstructionOperations) {
            map.put(constructionOperation.getName(), new PlayerGameConstructionOperations<>(constructionOperation, this));
        }
        this.gameConstructors = ImmutableMap.<String, PlayerGameConstructionOperations<?>> copyOf(map);
    }

    public PlayerSessionOperations getSessionOperations() {
        return playerSessionOperations;
    }

    public PlayerProfileOperations getProfileOperations() {
        return profileOperations;
    }

    public PlayerWalletOperations getWalletOperations() {
        return playerWalletOperations;
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    public PlayerProfile getProfile() {
        return profileOperations.get();
    }

    public Player setProfile(PlayerProfile newProfile) {
        profileOperations.set(newProfile);
        return this;
    }

    public PlayerIdentity getIdentity() {
        return identity;
    }

    public PlayerCredential getCredential() {
        return credential;
    }

    public PlayerSession getSession() {
        return session;
    }

    public <State extends GameState> PlayerGameConstructionOperations<State> getGameConstructor(String name) {
        return (PlayerGameConstructionOperations<State>) gameConstructors.get(name);
    }

    public Map<String, PlayerGameConstructionOperations<?>> getGameConstructors() {
        return gameConstructors;
    }

    // TODO When signing key must change, in order to provide safety, otherwise
    // Attacker can emulate user, by sending signed request back
    public <T> HttpEntity<T> sign(T value) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerId));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        return new HttpEntity<T>(value, header);
    }

    public <T> HttpEntity<T> signGame(long session, long table, T value) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(playerId));
        header.add("tableId", String.valueOf(table));
        header.add("sessionId", String.valueOf(session));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        return new HttpEntity<T>(value, header);
    }

    public void listen(long session, GameSessionListener sessionListener) {
        playerListenersManager.listen(session, sessionListener);
    }

    public void listen(GameConstruction construction, GameSessionListener sessionListener) {
        playerListenersManager.listen(construction, sessionListener);
    }

    public void close() {
        playerListenersManager.close();
    }

}
