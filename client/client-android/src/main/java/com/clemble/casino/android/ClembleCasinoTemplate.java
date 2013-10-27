package com.clemble.casino.android;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.android.event.listener.RabbitEventListenerManager;
import com.clemble.casino.android.game.service.AndroidGameActionService;
import com.clemble.casino.android.game.service.AndroidGameConstructionService;
import com.clemble.casino.android.payment.service.AndroidPaymentTransactionService;
import com.clemble.casino.android.player.service.AndroidPlayerPresenceService;
import com.clemble.casino.android.player.service.AndroidPlayerProfileService;
import com.clemble.casino.android.player.service.AndroidPlayerSessionService;
import com.clemble.casino.client.ClembleCasino;
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
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.event.listener.EventListenersManager;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.payment.service.PaymentTransactionService;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClembleCasinoTemplate implements ClembleCasino {

    final private String player;
    final private EventListenersManager eventListenersManager;
    final private PlayerSessionOperations playerSessionOperations;
    final private PlayerProfileOperations playerProfileOperations;
    final private PlayerPresenceOperations playerPresenceOperations;
    final private PaymentTransactionOperations paymentTransactionOperations;
    final private Map<Game, GameConstructionOperations<?>> gameToConstructionOperations;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ClembleCasinoTemplate(RestClientService restClient, ObjectMapper objectMapper) throws IOException {
        this.player = checkNotNull(restClient).getPlayer();

        this.playerSessionOperations = new SimplePlayerSessionOperations(player, new AndroidPlayerSessionService(restClient));
        ResourceLocations resourceLocations = checkNotNull(playerSessionOperations.create().getResourceLocations());
        ServerRegistryConfiguration registryConfiguration = resourceLocations.getServerRegistryConfiguration();

        // Step 1. Creating PlayerProfile service
        RestClientService playerRestService = restClient.construct(registryConfiguration.getPlayerRegistry());
        PlayerProfileService playerProfileService = new AndroidPlayerProfileService(playerRestService);
        this.playerProfileOperations = new SimplePlayerProfileOperations(player, playerProfileService);
        // Step 2. Creating PlayerPresence service
        PlayerPresenceService playerPresenceService = new AndroidPlayerPresenceService(playerRestService);
        this.playerPresenceOperations = new SimplePlayerPresenceOperations(player, playerPresenceService);
        // Step 3. Creating PaymentTransaction service
        RestClientService paymentRestService = restClient.construct(registryConfiguration.getPaymentRegistry());
        PaymentTransactionService paymentTransactionService = new AndroidPaymentTransactionService(paymentRestService);
        this.paymentTransactionOperations = new SimplePaymentTransactionOperations(player, paymentTransactionService);
        // Step 4. Creating GameConstruction services
        this.gameToConstructionOperations = new EnumMap<Game, GameConstructionOperations<?>>(Game.class);
        this.eventListenersManager = new RabbitEventListenerManager(resourceLocations.getNotificationConfiguration(), objectMapper);
        for (Game game : resourceLocations.getGames()) {
            ServerRegistry gameRegistry = registryConfiguration.getGameRegistry(game);
            RestClientService gameRestService = restClient.construct(gameRegistry);
            GameConstructionService constructionService = new AndroidGameConstructionService(gameRestService);
            GameActionService<?> gameActionService = new AndroidGameActionService<>(gameRestService);
            GameActionOperations<?> actionOperations = new SimpleGameActionOperations<>(player, null, eventListenersManager, gameActionService);
            GameConstructionOperations<?> constructionOperations = new SimpleGameConstructionOperations(player, game, actionOperations, constructionService, eventListenersManager);
            this.gameToConstructionOperations.put(game, constructionOperations);
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
    @SuppressWarnings("unchecked")
    public <T extends GameState> GameConstructionOperations<T> getGameConstructionOperations(Game game) {
        return (GameConstructionOperations<T>) gameToConstructionOperations.get(game);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <State extends GameState> GameActionOperations<State> getGameActionOperations(GameSessionKey session) {
        return (GameActionOperations<State>) gameToConstructionOperations.get(session.getGame()).getActionOperations(session);
    }

}
