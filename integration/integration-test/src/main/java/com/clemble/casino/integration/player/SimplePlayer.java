package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerOperation;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.payment.PaymentOperations;
import com.clemble.casino.client.payment.PaymentTemplate;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerProfileTemplate;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.client.player.PlayerSessionTemplate;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.integration.player.account.PaymentServiceFactory;
import com.clemble.casino.integration.player.profile.PlayerProfileServiceFactory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

public class SimplePlayer implements Player {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private String player;
    final private PlayerSession session;
    final private PlayerToken identity;
    final private EventListenerOperation playerListenersManager;
    final private PlayerCredential credential;

    final private Map<Game, GameConstructionOperations<?>> gameConstructors;

    final private PlayerSessionOperations playerSessionOperations;
    final private PaymentOperations playerAccountOperations;
    final private PlayerProfileOperations profileOperations;

    public SimplePlayer(
            final ObjectMapper objectMapper,
            final PlayerToken playerIdentity, 
            final PlayerCredential credential,
            final PlayerProfileServiceFactory playerProfileService,
            final PlayerSessionService sessionOperations,
            final PaymentServiceFactory accountOperations,
            final EventListenerOperationsFactory listenerOperations,
            final Collection<GameConstructionOperations<?>> playerConstructionOperations) {
        this.identity = checkNotNull(playerIdentity);
        this.player = playerIdentity.getPlayer();

        this.profileOperations = new PlayerProfileTemplate(player, playerProfileService.construct(this));
        this.playerAccountOperations = new PaymentTemplate(player, accountOperations.construct(this));

        this.playerSessionOperations = new PlayerSessionTemplate(player, sessionOperations);
        this.session = checkNotNull(playerSessionOperations.create());
        this.credential = checkNotNull(credential);
        this.playerListenersManager = listenerOperations.construct(session.getResourceLocations().getNotificationConfiguration(), objectMapper);

        Map<Game, GameConstructionOperations<?>> map = new HashMap<>();
        for (GameConstructionOperations<?> constructionOperation : playerConstructionOperations) {
            map.put(constructionOperation.getGame(), constructionOperation);
        }
        this.gameConstructors = ImmutableMap.<Game, GameConstructionOperations<?>> copyOf(map);
    }

    @Override
    public PlayerSessionOperations sessionOperations() {
        return playerSessionOperations;
    }

    @Override
    public PlayerProfileOperations profileOperations() {
        return profileOperations;
    }

    @Override
    public PaymentOperations paymentOperations() {
        return playerAccountOperations;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends PlayerProfile> T getProfile() {
        return (T) profileOperations.getPlayerProfile();
    }

    public Player setProfile(PlayerProfile newProfile) {
        profileOperations.updatePlayerProfile(newProfile);
        return this;
    }

    @Override
    public PlayerToken getIdentity() {
        return identity;
    }

    @Override
    public PlayerCredential getCredential() {
        return credential;
    }

    @Override
    public PlayerSession getSession() {
        return session;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <State extends GameState> GameConstructionOperations<State> gameConstructionOperations(Game game) {
        return (GameConstructionOperations<State>) gameConstructors.get(game);
    }

    @Override
    public Map<Game, GameConstructionOperations<?>> gameConstructionOperations() {
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

    @Override
    public void listen(GameSessionKey session, EventListener sessionListener) {
        playerListenersManager.subscribe(sessionListener);
    }

    @Override
    public void close() {
        playerListenersManager.close();
    }

}
