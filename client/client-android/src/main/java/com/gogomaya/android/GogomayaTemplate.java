package com.gogomaya.android;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.android.event.listener.RabbitEventListenerManager;
import com.gogomaya.android.game.service.AndroidGameActionService;
import com.gogomaya.android.game.service.AndroidGameConstructionService;
import com.gogomaya.android.payment.service.AndroidPaymentTransactionService;
import com.gogomaya.android.player.service.AndroidPlayerPresenceService;
import com.gogomaya.android.player.service.AndroidPlayerProfileService;
import com.gogomaya.android.player.service.AndroidPlayerSessionService;
import com.gogomaya.client.Gogomaya;
import com.gogomaya.client.game.service.GameActionOperations;
import com.gogomaya.client.game.service.GameConstructionOperations;
import com.gogomaya.client.game.service.SimpleGameActionOperations;
import com.gogomaya.client.game.service.SimpleGameConstructionOperations;
import com.gogomaya.client.payment.service.PaymentTransactionOperations;
import com.gogomaya.client.payment.service.SimplePaymentTransactionOperations;
import com.gogomaya.client.player.service.PlayerPresenceOperations;
import com.gogomaya.client.player.service.PlayerProfileOperations;
import com.gogomaya.client.player.service.PlayerSessionOperations;
import com.gogomaya.client.player.service.SimplePlayerPresenceOperations;
import com.gogomaya.client.player.service.SimplePlayerProfileOperations;
import com.gogomaya.client.player.service.SimplePlayerSessionOperations;
import com.gogomaya.client.service.RestClientService;
import com.gogomaya.configuration.GameLocation;
import com.gogomaya.configuration.ResourceLocations;
import com.gogomaya.event.listener.EventListenersManager;
import com.gogomaya.game.Game;
import com.gogomaya.game.GameSessionKey;
import com.gogomaya.game.GameState;
import com.gogomaya.game.service.GameConstructionService;
import com.gogomaya.payment.service.PaymentTransactionService;
import com.gogomaya.player.service.PlayerPresenceService;
import com.gogomaya.player.service.PlayerProfileService;

public class GogomayaTemplate implements Gogomaya {

    final private long playerId;
    final private RestClientService restClient;
    final private EventListenersManager eventListenersManager;
    final private PlayerSessionOperations playerSessionOperations;
    final private PlayerProfileOperations playerProfileOperations;
    final private PlayerPresenceOperations playerPresenceOperations;
    final private PaymentTransactionOperations paymentTransactionOperations;
    final private Map<Game, GameConstructionOperations> gameToConstructionOperations;

    public GogomayaTemplate(RestClientService restClient, ObjectMapper objectMapper) throws IOException {
        this.playerId = checkNotNull(restClient).getPlayerId();
        this.restClient = checkNotNull(restClient);

        this.playerSessionOperations = new SimplePlayerSessionOperations(playerId, new AndroidPlayerSessionService(restClient));
        ResourceLocations resourceLocations = checkNotNull(playerSessionOperations.create().getResourceLocations());

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
            final GameConstructionOperations constructionOperations = new SimpleGameConstructionOperations(playerId, location.getGame(), constructionService,
                    eventListenersManager);
            this.gameToConstructionOperations.put(location.getGame(), constructionOperations);
        }
    }

    @Override
    public PlayerProfileOperations getPlayerProfileOperations() {
        return playerProfileOperations;
    }

    @Override
    public PlayerPresenceOperations getPlayerPresenceOperations() {
        return playerPresenceOperations;
    }

    @Override
    public PlayerSessionOperations getPlayerSessionOperations() {
        return playerSessionOperations;
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
    public <State extends GameState> GameActionOperations<State> getGameActionOperations(GameSessionKey session) {
        // Step 1. Fetching game construction operations
        GameConstructionOperations constructionOperations = getGameConstructionOperations(session.getGame());
        // Step 2. Fetching action Server
        String actionServer = constructionOperations.getGameActionServer(session.getSession());
        // Step 3. Preparing new restService
        return new SimpleGameActionOperations<>(playerId, session, eventListenersManager, new AndroidGameActionService<State>(restClient.construct(actionServer)));
    }

}
