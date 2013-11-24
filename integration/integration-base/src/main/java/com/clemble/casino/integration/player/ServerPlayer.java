package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.payment.PaymentOperations;
import com.clemble.casino.client.payment.PaymentTemplate;
import com.clemble.casino.client.player.PlayerPresenceOperations;
import com.clemble.casino.client.player.PlayerPresenceTemplate;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerProfileTemplate;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.client.player.PlayerSessionTemplate;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

public class ServerPlayer implements ClembleCasinoOperations {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private String player;
    final private PlayerSession session;
    final private EventListenerOperations playerListenersManager;
    final private PlayerCredential credential;

    final private Map<Game, GameConstructionOperations<?>> gameConstructors;

    final private PlayerPresenceOperations playerPresenceOperations;
    final private PlayerSessionOperations playerSessionOperations;
    final private PaymentOperations playerAccountOperations;
    final private PlayerProfileOperations profileOperations;

    public ServerPlayer(final ObjectMapper objectMapper, final PlayerToken playerIdentity, final PlayerCredential credential,
            final PlayerProfileService playerProfileService, final PlayerSessionService sessionOperations, final PaymentService accountOperations,
            final EventListenerOperationsFactory listenerOperations, final PlayerPresenceService playerPresenceService,
            final Collection<GameConstructionOperations<?>> playerConstructionOperations) {
        this.player = playerIdentity.getPlayer();
        this.playerSessionOperations = new PlayerSessionTemplate(player, sessionOperations);
        this.session = checkNotNull(playerSessionOperations.create());

        this.playerPresenceOperations = new PlayerPresenceTemplate(player, playerPresenceService);

        this.profileOperations = new PlayerProfileTemplate(player, playerProfileService);
        this.playerAccountOperations = new PaymentTemplate(player, accountOperations);

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
    public <State extends GameState> GameConstructionOperations<State> gameConstructionOperations(Game game) {
        return (GameConstructionOperations<State>) gameConstructors.get(game);
    }

    @Override
    public Map<Game, GameConstructionOperations<?>> gameConstructionOperations() {
        return gameConstructors;
    }

    @Override
    public void close() {
        playerListenersManager.close();
    }

    @Override
    public PlayerPresenceOperations presenceOperations() {
        return playerPresenceOperations;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <State extends GameState> GameActionOperations<State> gameActionOperations(GameSessionKey session) {
        return (GameActionOperations<State>) gameConstructors.get(session.getGame()).<State> getActionOperations(session.getSession());
    }

    @Override
    public RestTemplate getRestTemplate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAuthorized() {
        return true;
    }

    @Override
    public EventListenerOperations listenerOperations() {
        return playerListenersManager;
    }

}
