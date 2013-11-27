package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameActionTemplateFactory;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.game.GameConstructionTemplate;
import com.clemble.casino.client.payment.PaymentOperations;
import com.clemble.casino.client.payment.PaymentTemplate;
import com.clemble.casino.client.player.PlayerPresenceOperations;
import com.clemble.casino.client.player.PlayerPresenceTemplate;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerProfileTemplate;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.client.player.PlayerSessionTemplate;
import com.clemble.casino.event.Event;
import com.clemble.casino.game.Game;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.game.service.GameSpecificationService;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ServerPlayer implements ClembleCasinoOperations {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private String player;
    final private PlayerSession session;
    final private EventListenerOperations playerListenersManager;
    final private PlayerCredential credential;

    final private GameSpecificationService specificationService;
    final private GameConstructionService constructionService;
    final private GameActionService<?> actionService;
    final private LoadingCache<Game, GameConstructionOperations<?>> gameConstructors = CacheBuilder.newBuilder().build(new CacheLoader<Game, GameConstructionOperations<?>>(){

        @Override
        public GameConstructionOperations<?> load(Game key) throws Exception {
            return new GameConstructionTemplate<>(player, key, new GameActionTemplateFactory(player, playerListenersManager, actionService), constructionService, specificationService, playerListenersManager);
        }

    });

    final private PlayerPresenceOperations playerPresenceOperations;
    final private PlayerSessionOperations playerSessionOperations;
    final private PaymentOperations playerAccountOperations;
    final private PlayerProfileOperations profileOperations;

    public ServerPlayer(
            final ObjectMapper objectMapper,
            final PlayerToken playerIdentity,
            final PlayerCredential credential,
            final PlayerProfileService playerProfileService,
            final PlayerSessionService sessionOperations,
            final PaymentService accountOperations,
            final EventListenerOperationsFactory listenerOperations,
            final PlayerPresenceService playerPresenceService,
            final GameConstructionService gameConstructionService,
            final GameSpecificationService specificationService,
            final GameActionService<?> actionService) {
        this.player = playerIdentity.getPlayer();
        this.playerSessionOperations = new PlayerSessionTemplate(player, sessionOperations);
        this.session = checkNotNull(playerSessionOperations.create());

        this.playerPresenceOperations = new PlayerPresenceTemplate(player, playerPresenceService);

        this.profileOperations = new PlayerProfileTemplate(player, playerProfileService);
        this.playerAccountOperations = new PaymentTemplate(player, accountOperations);

        this.credential = checkNotNull(credential);
        this.playerListenersManager = listenerOperations.construct(session.getResourceLocations().getNotificationConfiguration(), objectMapper);
        this.playerListenersManager.subscribe(new EventListener() {

            final private AtomicInteger messageNum = new AtomicInteger();

            @Override
            public void onEvent(Event event) {
                System.out.println(messageNum.incrementAndGet() + " >> " + player + " >> " + event);
            }
        });

        this.constructionService = checkNotNull(gameConstructionService);
        this.specificationService = checkNotNull(specificationService);
        this.actionService = checkNotNull(actionService);
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
        return (GameConstructionOperations<State>) gameConstructors.getUnchecked(game);
    }

    @Override
    public Map<Game, GameConstructionOperations<?>> gameConstructionOperations() {
        return gameConstructors.asMap();
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
        return (GameActionOperations<State>) gameConstructors.getUnchecked(session.getGame()).getActionOperations(session.getSession());
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

