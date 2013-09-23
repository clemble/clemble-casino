package com.gogomaya.android;

import static com.gogomaya.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.android.event.listener.RabbitEventListenerManager;
import com.gogomaya.android.game.service.AndroidGameConstructionService;
import com.gogomaya.android.payment.service.AndroidPaymentTransactionService;
import com.gogomaya.android.player.service.AndroidPlayerProfileService;
import com.gogomaya.android.service.AndroidRestService;
import com.gogomaya.client.Gogomaya;
import com.gogomaya.client.game.service.GameConstructionOperations;
import com.gogomaya.client.game.service.SimpleGameConstructionOperations;
import com.gogomaya.client.payment.service.PaymentTransactionOperations;
import com.gogomaya.client.payment.service.SimplePaymentTransactionOperations;
import com.gogomaya.client.player.service.PlayerProfileOperations;
import com.gogomaya.client.player.service.PlayerSecurityClientService;
import com.gogomaya.client.player.service.SimplePlayerProfileOperations;
import com.gogomaya.client.service.RestClientService;
import com.gogomaya.configuration.GameLocation;
import com.gogomaya.configuration.ResourceLocations;
import com.gogomaya.event.listener.EventListenersManager;
import com.gogomaya.game.Game;
import com.gogomaya.game.service.GameConstructionService;
import com.gogomaya.payment.service.PaymentTransactionService;
import com.gogomaya.player.security.PlayerSession;
import com.gogomaya.player.service.PlayerProfileService;

public class GogomayaTemplate implements Gogomaya {

    final private EventListenersManager eventListenersManager;
    final private PlayerProfileOperations playerProfileOperations;
    final private PaymentTransactionOperations paymentTransactionOperations;
    final private Map<Game, GameConstructionOperations> gameToConstructionOperations;

    public GogomayaTemplate(PlayerSession playerSession, PlayerSecurityClientService<HttpEntity<?>> securityClientService, ObjectMapper objectMapper) throws IOException {
        RestClientService restClient = new AndroidRestService("", objectMapper, securityClientService);

        long playerId = checkNotNull(playerSession).getPlayerId();
        ResourceLocations resourceLocations = checkNotNull(playerSession.getResourceLocations());
        // Step 1. Creating PlayerProfile service
        RestClientService playerRestService = restClient.construct(resourceLocations.getPlayerProfileEndpoint());
        PlayerProfileService playerProfileService = new AndroidPlayerProfileService(playerRestService);
        this.playerProfileOperations = new SimplePlayerProfileOperations(playerId, playerProfileService);
        // Step 2. Creating PaymentTransaction service
        RestClientService paymentRestService = restClient.construct(resourceLocations.getPaymentEndpoint());
        PaymentTransactionService paymentTransactionService = new AndroidPaymentTransactionService(paymentRestService);
        this.paymentTransactionOperations = new SimplePaymentTransactionOperations(playerId, paymentTransactionService);
        // Step 3. Creating GameConstruction services
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

}
