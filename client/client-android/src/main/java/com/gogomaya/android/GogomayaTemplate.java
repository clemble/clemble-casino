package com.gogomaya.android;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.android.event.listener.RabbitEventListenerManager;
import com.gogomaya.android.game.service.AndroidGameActionService;
import com.gogomaya.android.game.service.AndroidGameConstructionService;
import com.gogomaya.android.payment.service.AndroidPaymentTransactionService;
import com.gogomaya.android.player.service.AndroidPlayerPresenceService;
import com.gogomaya.android.player.service.AndroidPlayerProfileService;
import com.gogomaya.android.service.AndroidRestService;
import com.gogomaya.client.Gogomaya;
import com.gogomaya.client.game.service.GameActionOperations;
import com.gogomaya.client.game.service.GameConstructionOperations;
import com.gogomaya.client.game.service.SimpleGameActionOperations;
import com.gogomaya.client.game.service.SimpleGameConstructionOperations;
import com.gogomaya.client.payment.service.PaymentTransactionOperations;
import com.gogomaya.client.payment.service.SimplePaymentTransactionOperations;
import com.gogomaya.client.player.service.PlayerPresenceOperations;
import com.gogomaya.client.player.service.PlayerProfileOperations;
import com.gogomaya.client.player.service.PlayerSecurityClientService;
import com.gogomaya.client.player.service.SimplePlayerPresenceOperations;
import com.gogomaya.client.player.service.SimplePlayerProfileOperations;
import com.gogomaya.client.service.RestClientService;
import com.gogomaya.configuration.GameLocation;
import com.gogomaya.configuration.ResourceLocations;
import com.gogomaya.event.listener.EventListenersManager;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameState;
import com.gogomaya.game.service.GameConstructionService;
import com.gogomaya.payment.service.PaymentTransactionService;
import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.player.service.PlayerPresenceService;
import com.gogomaya.player.service.PlayerProfileService;

public class GogomayaTemplate implements Gogomaya {

    final private long playerId;
    final private RestClientService restClient;
    final private EventListenersManager eventListenersManager;
    final private PlayerProfileOperations playerProfileOperations;
    final private PlayerPresenceOperations playerPresenceOperations;
    final private PaymentTransactionOperations paymentTransactionOperations;
    final private Map<Game, GameConstructionOperations> gameToConstructionOperations;

    public GogomayaTemplate(PlayerSession playerSession, PlayerSecurityClientService<HttpEntity<?>> securityClientService, ObjectMapper objectMapper) throws IOException {
        ResourceLocations resourceLocations = checkNotNull(playerSession.getResourceLocations());
        
        this.restClient = new AndroidRestService("", objectMapper, securityClientService);
        this.playerId = checkNotNull(playerSession).getPlayerId();
        // Step 1. Creating PlayerProfile service
        RestClientService playerRestService = restClient.construct(resourceLocations.getPlayerProfileEndpoint());
        PlayerProfileService playerProfileService = new AndroidPlayerProfileService(playerRestService);
        this.playerProfileOperations = new SimplePlayerProfileOperations(playerId, playerProfileService);
        // Step 2. Creating PlayerPresence service
        PlayerPresenceService playerPresenceService = new AndroidPlayerPresenceService(playerRestService);
        this.playerPresenceOperations = new SimplePlayerPresenceOperations(playerId, playerPresenceService);
        // Step 3. Creating PaymentTransaction service
        RestClientService paymentRestService = restClient.construct(resourceLocations.getPaymentEndpoint());
        PaymentTransactionService paymentTransactionService = new AndroidPaymentTransactionService(paymentRestService);
        this.paymentTransactionOperations = new SimplePaymentTransactionOperations(playerId, paymentTransactionService);
        // Step 4. Creating GameConstruction services
        this.gameToConstructionOperations = new HashMap<Game, GameConstructionOperations>();
        this.eventListenersManager = new RabbitEventListenerManager(resourceLocations.getNotificationConfiguration(), objectMapper);
        for (GameLocation location : resourceLocations.getGameLocations()) {
            final RestClientService gameRestService = restClient.construct(location.getUrl());
            final GameConstructionService constructionService = new AndroidGameConstructionService(gameRestService);
            final GameConstructionOperations constructionOperations = new SimpleGameConstructionOperations(playerId, constructionService, eventListenersManager);
            this.gameToConstructionOperations.put(location.getGame(), constructionOperations);
        }
    }

    @Override
    public PlayerProfileOperations getPlayerProfileOperations() {
        return playerProfileOperations;
    }

    @Override
    public PaymentTransactionOperations getPaymentTransactionOperations() {
        return paymentTransactionOperations;
    }

    @Override
    public GameConstructionOperations getGameConstructionOperations(Game game) {
        return gameToConstructionOperations.get(game);
    }

    @Override
    public <State extends GameState> GameActionOperations<State> getGameActionOperations(Game game, long sessionId) {
        // Step 1. Fetching game construction operations
        GameConstructionOperations constructionOperations = getGameConstructionOperations(game);
        // Step 2. Fetching action Server
        String actionServer = constructionOperations.getGameActionServer(sessionId);
        // Step 3. Preparing new restService
        return new SimpleGameActionOperations<>(playerId, sessionId, eventListenersManager, new AndroidGameActionService<State>(restClient.construct(actionServer)));
    }

    @Override
    public PlayerPresenceOperations getPlayerPresenceOperations() {
        return playerPresenceOperations;
    }

}
