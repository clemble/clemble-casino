package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.web.client.RestTemplate;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.event.EventListener;
import com.clemble.casino.client.event.EventListenerOperations;
import com.clemble.casino.client.event.EventTypeSelector;
import com.clemble.casino.client.game.GameActionOperations;
import com.clemble.casino.client.game.GameActionOperationsFactory;
import com.clemble.casino.client.game.GameActionTemplateFactory;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.client.game.GameConstructionTemplate;
import com.clemble.casino.client.game.GameRecordOperations;
import com.clemble.casino.client.game.GameRecordTemplate;
import com.clemble.casino.client.payment.PaymentOperations;
import com.clemble.casino.client.payment.PaymentTemplate;
import com.clemble.casino.client.player.PlayerConnectionOperations;
import com.clemble.casino.client.player.PlayerConnectionTemplate;
import com.clemble.casino.client.player.PlayerPresenceOperations;
import com.clemble.casino.client.player.PlayerPresenceTemplate;
import com.clemble.casino.client.player.PlayerProfileOperations;
import com.clemble.casino.client.player.PlayerProfileTemplate;
import com.clemble.casino.client.player.PlayerSessionOperations;
import com.clemble.casino.client.player.PlayerSessionTemplate;
import com.clemble.casino.game.GameSessionKey;
import com.clemble.casino.game.GameState;
import com.clemble.casino.game.event.server.GameInitiatedEvent;
import com.clemble.casino.game.service.AutoGameConstructionService;
import com.clemble.casino.game.service.AvailabilityGameConstructionService;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.GameConfigurationService;
import com.clemble.casino.game.service.GameInitiationService;
import com.clemble.casino.game.service.GameRecordService;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerCasinoOperations implements ClembleCasinoOperations {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private String player;
    final private PlayerSession session;
    final private EventListenerOperations listenerOperations;

    final private AutoGameConstructionService constructionService;
    final private GameConstructionOperations gameConstructors;
    final private PlayerPresenceOperations playerPresenceOperations;
    final private PlayerConnectionOperations connectionOperations;
    final private PlayerSessionOperations playerSessionOperations;
    final private PaymentOperations playerAccountOperations;
    final private PlayerProfileOperations profileOperations;
    final private GameActionOperationsFactory actionOperationsFactory;
    final private GameRecordOperations recordOperations;

    public ServerCasinoOperations(final ObjectMapper objectMapper, final PlayerToken playerIdentity, final PlayerCredential credential,
            final PlayerProfileService playerProfileService, final PlayerConnectionService playerConnectionService,
            final PlayerSessionService sessionOperations, final PaymentService accountOperations,
            final EventListenerOperationsFactory listenerOperationsFactory, final PlayerPresenceService playerPresenceService,
            final AutoGameConstructionService gameConstructionService, final AvailabilityGameConstructionService availabilityConstructionService,
            final GameInitiationService initiationService, final GameConfigurationService specificationService, final GameActionService actionService,
            final GameRecordService recordService) {
        this.player = playerIdentity.getPlayer();
        this.playerSessionOperations = new PlayerSessionTemplate(player, sessionOperations);
        this.session = checkNotNull(playerSessionOperations.create());
        this.listenerOperations = listenerOperationsFactory.construct(player, session.getResourceLocations().getNotificationConfiguration(), objectMapper);

        this.playerPresenceOperations = new PlayerPresenceTemplate(player, playerPresenceService, listenerOperations);

        this.profileOperations = new PlayerProfileTemplate(player, playerProfileService);
        this.connectionOperations = new PlayerConnectionTemplate(player, playerConnectionService, profileOperations);
        this.playerAccountOperations = new PaymentTemplate(player, accountOperations, listenerOperations);

        this.constructionService = checkNotNull(gameConstructionService);

        this.gameConstructors = new GameConstructionTemplate(player, constructionService, availabilityConstructionService, initiationService, specificationService, listenerOperations);
        this.actionOperationsFactory = new GameActionTemplateFactory(player, listenerOperations, actionService);

        this.listenerOperations.subscribe(new EventTypeSelector(GameInitiatedEvent.class), new EventListener<GameInitiatedEvent>() {
            @Override
            public void onEvent(GameInitiatedEvent event) {
                // Step 1. Fetching session key
                GameSessionKey sessionKey = event.getSession();
                initiationService.confirm(sessionKey.getGame(), sessionKey.getSession(), player);
            }
        });

        this.recordOperations = new GameRecordTemplate(recordService);
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
    public PlayerConnectionOperations connectionOperations() {
        return connectionOperations;
    }

    @Override
    public PaymentOperations paymentOperations() {
        return playerAccountOperations;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public GameConstructionOperations gameConstructionOperations() {
        return gameConstructors;
    }

    @Override
    public void close() {
        listenerOperations.close();
    }

    @Override
    public PlayerPresenceOperations presenceOperations() {
        return playerPresenceOperations;
    }

    @Override
    public <State extends GameState> GameActionOperations<State> gameActionOperations(GameSessionKey session) {
        return (GameActionOperations<State>) actionOperationsFactory.<State> construct(session);
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
        return listenerOperations;
    }

    @Override
    public GameRecordOperations gameRecordOperations() {
        return recordOperations;
    }

}