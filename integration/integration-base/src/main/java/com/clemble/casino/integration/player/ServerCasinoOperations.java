package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.integration.goal.IntegrationGoalService;
import com.clemble.casino.client.player.*;
import com.clemble.casino.goal.service.GoalService;
import com.clemble.casino.integration.payment.IntegrationPlayerAccountService;
import com.clemble.casino.payment.service.PaymentTransactionOperations;
import com.clemble.casino.payment.service.PlayerAccountService;
import com.clemble.casino.player.service.*;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import com.clemble.casino.server.goal.controller.GoalServiceController;
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;
import com.clemble.casino.server.payment.controller.PlayerAccountServiceController;
import com.clemble.casino.server.profile.controller.PlayerImageServiceController;
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
import com.clemble.casino.integration.payment.IntegrationPaymentTransactionService;
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
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerSession;
import com.clemble.casino.player.security.PlayerToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerCasinoOperations implements ClembleCasinoOperations {

    /**
     * Generated
     */
    private static final long serialVersionUID = -4160641502466429770L;

    final private String host;
    final private String player;
    final private PlayerSession session;
    final private EventListenerOperations listenerOperations;

    final private AutoGameConstructionService constructionService;
    final private GameConstructionOperations gameConstructors;
    final private PlayerPresenceOperations playerPresenceOperations;
    final private PlayerProfileOperations profileOperations;
    final private PlayerImageService imageOperations;
    final private PlayerConnectionService connectionOperations;
    final private PlayerSessionOperations playerSessionOperations;
    final private PlayerAccountService accountService;
    final private PaymentTransactionOperations paymentTransactionOperations;
    final private GameActionOperationsFactory actionOperationsFactory;
    final private GameRecordOperations recordOperations;
    final private GoalService goalService;

    public ServerCasinoOperations(
        final String host,
        final ObjectMapper objectMapper,
        final PlayerToken playerIdentity,
        final PlayerCredential credential,
        final PlayerProfileService playerProfileService,
        final PlayerImageServiceController imageService,
        final PlayerConnectionServiceController playerConnectionService,
        final PlayerSessionService sessionOperations,
        final PlayerAccountServiceController accountServiceController,
        final PaymentTransactionServiceController paymentTransactionService,
        final EventListenerOperationsFactory listenerOperationsFactory,
        final PlayerPresenceService playerPresenceService,
        final AutoGameConstructionService gameConstructionService,
        final AvailabilityGameConstructionService availabilityConstructionService,
        final GameInitiationService initiationService,
        final GameConfigurationService specificationService,
        final GameActionService actionService,
        final GameRecordService recordService,
        final GoalServiceController goalService
    ) {
        this.host = host;
        this.player = playerIdentity.getPlayer();
        this.playerSessionOperations = new PlayerSessionTemplate(player, sessionOperations);
        this.session = checkNotNull(playerSessionOperations.create());
        this.listenerOperations = listenerOperationsFactory.construct(player, host, objectMapper);

        this.playerPresenceOperations = new PlayerPresenceTemplate(player, playerPresenceService, listenerOperations);

        this.profileOperations = new PlayerProfileTemplate(player, playerProfileService);
        this.imageOperations = new IntegrationPlayerImageService(player, imageService);
        this.connectionOperations = new IntegrationPlayerConnectionService(player, playerConnectionService);
        this.paymentTransactionOperations = new PaymentTransactionOperations(new IntegrationPaymentTransactionService(player, paymentTransactionService));
        this.accountService = new IntegrationPlayerAccountService(player, accountServiceController);

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
        this.goalService = new IntegrationGoalService(player, goalService);
    }

    @Override
    public String getHost() {
        return host;
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
    public PlayerImageService imageOperations() {
        return imageOperations;
    }

    @Override
    public PlayerConnectionService connectionOperations() {
        return connectionOperations;
    }

    @Override
    public PlayerAccountService accountService() {
        return accountService;
    }

    @Override
    public PaymentTransactionOperations paymentOperations() {
        return paymentTransactionOperations;
    }

    @Override
    public GoalService goalOperations() {
        return goalService;
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
