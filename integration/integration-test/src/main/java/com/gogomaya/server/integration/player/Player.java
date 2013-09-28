package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.construct.GameConstruction;
import com.gogomaya.player.PlayerAware;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.server.integration.game.GameSessionListener;
import com.gogomaya.server.integration.game.construction.GameConstructionOperations;
import com.gogomaya.server.integration.game.construction.PlayerGameConstructionOperations;
import com.gogomaya.server.integration.player.account.AccountOperations;
import com.gogomaya.server.integration.player.account.PlayerAccountOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerManager;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.PlayerProfileOperations;
import com.gogomaya.server.integration.player.profile.ProfileOperations;
import com.gogomaya.server.integration.player.session.PlayerSessionOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.google.common.collect.ImmutableMap;

public class Player implements PlayerAware {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private String player;
    final private PlayerSession session;
    final private PlayerIdentity identity;
    final private PlayerListenerManager playerListenersManager;
    final private PlayerCredential credential;

    final private Map<Game, PlayerGameConstructionOperations<?>> gameConstructors;

    final private PlayerSessionOperations playerSessionOperations;
    final private PlayerAccountOperations playerWalletOperations;
    final private PlayerProfileOperations profileOperations;

    public Player(final PlayerIdentity playerIdentity,
            final PlayerCredential credential,
            final ProfileOperations playerProfileOperations,
            final SessionOperations sessionOperations,
            final AccountOperations accountOperations,
            final PlayerListenerOperations listenerOperations,
            final Collection<GameConstructionOperations<?>> playerConstructionOperations) {
        this.profileOperations = new PlayerProfileOperations(this, playerProfileOperations);
        this.playerSessionOperations = new PlayerSessionOperations(this, sessionOperations);
        this.playerWalletOperations = new PlayerAccountOperations(this, accountOperations);

        this.identity = checkNotNull(playerIdentity);
        this.player = identity.getPlayer();
        this.session = checkNotNull(playerSessionOperations.start());
        this.credential = checkNotNull(credential);
        this.playerListenersManager = new PlayerListenerManager(this, listenerOperations);

        Map<Game, PlayerGameConstructionOperations<?>> map = new HashMap<>();
        for (GameConstructionOperations<?> constructionOperation : playerConstructionOperations) {
            map.put(constructionOperation.getGame(), new PlayerGameConstructionOperations<>(constructionOperation, this));
        }
        this.gameConstructors = ImmutableMap.<Game, PlayerGameConstructionOperations<?>> copyOf(map);
    }

    public PlayerSessionOperations getSessionOperations() {
        return playerSessionOperations;
    }

    public PlayerProfileOperations getProfileOperations() {
        return profileOperations;
    }

    public PlayerAccountOperations getWalletOperations() {
        return playerWalletOperations;
    }

    @Override
    public String getPlayer() {
        return player;
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

    @SuppressWarnings("unchecked")
    public <State extends GameState> PlayerGameConstructionOperations<State> getGameConstructor(Game game) {
        return (PlayerGameConstructionOperations<State>) gameConstructors.get(game);
    }

    public Map<Game, PlayerGameConstructionOperations<?>> getGameConstructors() {
        return gameConstructors;
    }

    // TODO When signing key must change, in order to provide safety, otherwise
    // Attacker can emulate user, by sending signed request back
    public <T> HttpEntity<T> sign(T value) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        return new HttpEntity<T>(value, header);
    }

    public <T> HttpEntity<T> signGame(GameSessionKey session, T value) {
        // Step 1. Creating Header
        MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
        header.add("playerId", String.valueOf(player));
        header.add("sessionId", String.valueOf(session));
        header.add("Content-Type", "application/json");
        // Step 2. Generating request
        return new HttpEntity<T>(value, header);
    }

    public void listen(GameSessionKey session, GameSessionListener sessionListener) {
        playerListenersManager.listen(session, sessionListener);
    }

    public void listen(GameConstruction construction, GameSessionListener sessionListener) {
        playerListenersManager.listen(construction, sessionListener);
    }

    public void close() {
        playerListenersManager.close();
    }

}
