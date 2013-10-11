package com.clemble.casino.android;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.android.event.listener.RabbitEventListenerManager;
import com.clemble.casino.android.game.service.AndroidGameActionService;
import com.clemble.casino.android.game.service.AndroidGameConstructionService;
import com.clemble.casino.android.payment.service.AndroidPaymentTransactionService;
import com.clemble.casino.android.player.service.AndroidPlayerPresenceService;
import com.clemble.casino.android.player.service.AndroidPlayerProfileService;
import com.clemble.casino.android.player.service.AndroidPlayerSessionService;
import com.clemble.casino.configuration.GameLocation;
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.clemble.casino.client.Gogomaya;
import com.clemble.casino.client.game.service.GameActionOperations;
import com.clemble.casino.client.game.service.GameConstructionOperations;
import com.clemble.casino.client.game.service.SimpleGameActionOperations;
import com.clemble.casino.client.game.service.SimpleGameConstructionOperations;
import com.clemble.casino.client.payment.service.PaymentTransactionOperations;
import com.clemble.casino.client.payment.service.SimplePaymentTransactionOperations;
import com.clemble.casino.client.player.service.PlayerPresenceOperations;
import com.clemble.casino.client.player.service.PlayerProfileOperations;
import com.clemble.casino.client.player.service.PlayerSessionOperations;
import com.clemble.casino.client.player.service.SimplePlayerPresenceOperations;
import com.clemble.casino.client.player.service.SimplePlayerProfileOperations;
import com.clemble.casino.client.player.service.SimplePlayerSessionOperations;
import com.clemble.casino.client.service.RestClientService;
import com.clemble.casino.event.listener.EventListenersManager;

public class GogomayaTemplate implements Gogomaya {

    final private String player;
    final private RestClientService restClient;
    final private EventListenersManager eventListenersManager;
    final private PlayerSessionOperations playerSessionOperations;
    final private PlayerProfileOperations playerProfileOperations;
    final private PlayerPresenceOperations playerPresenceOperations;
    final private PaymentTransactionOperations paymentTransactionOperations;
    final private Map<Game, GameConstructionOperations> gameToConstructionOperations;

    public GogomayaTemplate(RestClientService restClient, ObjectMapper objectMapper) throws IOException {
        this.player = checkNotNull(restClient).getPlayer();
        this.restClient = checkNotNull(restClient);

        this.playerSessionOperations = new SimplePlayerSessionOperations(player, new AndroidPlayerSessionService(restClient));
        ResourceLocations resourceLocations = checkNotNull(playerSessionOperations.create().getResourceLocations());

        // Step 1. Creating PlayerProfile service
        RestClientService playerRestService = restClient.construct(resourceLocations.getPlayerProfileEndpoint());
        PlayerProfileService playerProfileService = new AndroidPlayerProfileService(playerRestService);
        this.playerProfileOperations = new SimplePlayerProfileOperations(player, playerProfileService);
        // Step 2. Creating PlayerPresence service
        PlayerPresenceService playerPresenceService = new AndroidPlayerPresenceService(playerRestService);
        this.playerPresenceOperations = new SimplePlayerPresenceOperations(player, playerPresenceService);
        // Step 3. Creating PaymentTransaction service
        RestClientService paymentRestService = restClient.construct(resourceLocations.getPaymentEndpoint());
        PaymentTransactionService paymentTransactionService = new AndroidPaymentTransactionService(paymentRestService);
        this.paymentTransactionOperations = new SimplePaymentTransactionOperations(player, paymentTransactionService);
        // Step 4. Creating GameConstruction services
        this.gameToConstructionOperations = new HashMap<Game, GameConstructionOperations>();
        this.eventListenersManager = new RabbitEventListenerManager(resourceLocations.getNotificationConfiguration(), objectMapper);
        for (GameLocation location : resourceLocations.getGameLocations()) {
            final RestClientService gameRestService = restClient.construct(location.getUrl());
            final GameConstructionService constructionService = new AndroidGameConstructionService(gameRestService);
            final GameConstructionOperations constructionOperations = new SimpleGameConstructionOperations(player, location.getGame(), constructionService, eventListenersManager);
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
        return new SimpleGameActionOperations<>(player, session, eventListenersManager, new AndroidGameActionService<State>(restClient.construct(actionServer)));
    }

}
