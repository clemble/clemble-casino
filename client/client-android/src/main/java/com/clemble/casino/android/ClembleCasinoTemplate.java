package com.clemble.casino.android;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;
import org.springframework.social.oauth1.RSARequestSigner;
import org.springframework.web.client.RestTemplate;

import com.clemble.casino.ServerRegistry;
import com.clemble.casino.SingletonRegistry;
import com.clemble.casino.android.event.listener.RabbitEventListenerManager;
import com.clemble.casino.android.game.service.AndroidGameActionTemplate;
import com.clemble.casino.android.game.service.AndroidGameConstructionService;
import com.clemble.casino.android.game.service.GameActionTemplate;
import com.clemble.casino.android.payment.AndroidPaymentTransactionService;
import com.clemble.casino.android.player.AndroidPlayerPresenceService;
import com.clemble.casino.android.player.AndroidPlayerProfileService;
import com.clemble.casino.android.player.AndroidPlayerSessionService;
import com.clemble.casino.android.player.PlayerPresenceTemplate;
import com.clemble.casino.client.ClembleCasino;
import com.clemble.casino.client.error.ClembleCasinoRestErrorHandler;
import com.clemble.casino.client.event.EventListenersManager;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.game.GameConstructionTemplate;
import com.clemble.casino.client.payment.PaymentTransactionOperations;
import com.clemble.casino.client.payment.PaymentTransactionTemplate;
import com.clemble.casino.client.player.PlayerPresenceOperations;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerProfileTemplate;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.client.player.PlayerSessionTemplate;
import com.clemble.casino.configuration.ResourceLocations;
import com.clemble.casino.configuration.ServerRegistryConfiguration;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;

public class ClembleCasinoTemplate extends AbstractOAuth1ApiBinding implements ClembleCasino {

    final private EventListenersManager eventListenersManager;
    final private PlayerSessionOperations playerSessionOperations;
    final private PlayerProfileOperations playerProfileOperations;
    final private PlayerPresenceOperations playerPresenceOperations;
    final private PaymentTransactionOperations paymentTransactionOperations;
    final private Map<Game, GameConstructionOperations<?>> gameToConstructionOperations;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ClembleCasinoTemplate(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret, String player, String managementUrl)
            throws IOException {
        super(consumerKey, consumerSecret, accessToken, accessTokenSecret, new RSARequestSigner());

        this.playerSessionOperations = new PlayerSessionTemplate(player, new AndroidPlayerSessionService(getRestTemplate(), new SingletonRegistry(managementUrl)));
        ResourceLocations resourceLocations = checkNotNull(playerSessionOperations.create().getResourceLocations());
        ServerRegistryConfiguration registryConfiguration = resourceLocations.getServerRegistryConfiguration();

        // Step 1. Creating PlayerProfile service
        PlayerProfileService playerProfileService = new AndroidPlayerProfileService(getRestTemplate(), registryConfiguration.getPlayerRegistry());
        this.playerProfileOperations = new PlayerProfileTemplate(player, playerProfileService);
        // Step 2. Creating PlayerPresence service
        PlayerPresenceService playerPresenceService = new AndroidPlayerPresenceService(getRestTemplate(), registryConfiguration.getPlayerRegistry());
        this.playerPresenceOperations = new PlayerPresenceTemplate(player, playerPresenceService);
        // Step 3. Creating PaymentTransaction service
        ServerRegistry paymentServerRegistry = registryConfiguration.getPaymentRegistry();
        PaymentService paymentTransactionService = new AndroidPaymentTransactionService(getRestTemplate(), paymentServerRegistry);
        this.paymentTransactionOperations = new PaymentTransactionTemplate(player, paymentTransactionService);
        // Step 4. Creating GameConstruction services
        this.gameToConstructionOperations = new EnumMap<Game, GameConstructionOperations<?>>(Game.class);
        this.eventListenersManager = new RabbitEventListenerManager(resourceLocations.getNotificationConfiguration(), ClembleCasinoConstants.OBJECT_MAPPER);
        for (Game game : resourceLocations.getGames()) {
            ServerRegistry gameRegistry = registryConfiguration.getGameRegistry(game);
            GameConstructionService constructionService = new AndroidGameConstructionService(getRestTemplate(), gameRegistry);
            GameActionService<?> gameActionService = new AndroidGameActionTemplate<>(getRestTemplate(), gameRegistry);
            GameActionOperations<?> actionOperations = new GameActionTemplate<>(player, null, eventListenersManager, gameActionService);
            GameConstructionOperations<?> constructionOperations = new GameConstructionTemplate(player, game, actionOperations, constructionService,
                    eventListenersManager);
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

    /**
     * Returns a {@link MappingJackson2HttpMessageConverter} to be used by the internal {@link RestTemplate}. Override to customize the message converter (for
     * example, to set a custom object mapper or supported media types). To remove/replace this or any of the other message converters that are registered by
     * default, override the getMessageConverters() method instead.
     */
    @Override
    protected MappingJackson2HttpMessageConverter getJsonMessageConverter() {
        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
        jacksonConverter.setObjectMapper(ClembleCasinoConstants.OBJECT_MAPPER);
        return jacksonConverter;
    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new ClembleCasinoRestErrorHandler(ClembleCasinoConstants.OBJECT_MAPPER));
    }
}
