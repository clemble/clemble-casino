package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.game.construction.service.AvailabilityGameConstructionService;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.construction.service.AutoGameConstructionService;
import com.clemble.casino.game.configuration.service.GameConfigurationService;
import com.clemble.casino.game.service.GameRecordService;
import com.clemble.casino.goal.controller.GoalJudgeDutyServiceController;
import com.clemble.casino.goal.controller.GoalJudgeInvitationServiceController;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.integration.goal.IntegrationGoalOperationsFactory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.game.construction.controller.AutoGameConstructionController;
import com.clemble.casino.server.game.construction.controller.AvailabilityGameConstructionController;
import com.clemble.casino.server.game.construction.controller.GameInitiationServiceController;
import com.clemble.casino.server.presence.controller.PlayerSessionServiceController;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.security.ClembleConsumerDetails;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.PlayerToken;
import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.registration.PlayerSocialRegistrationRequest;
import com.clemble.casino.registration.service.PlayerFacadeRegistrationService;
import com.clemble.casino.server.connection.controller.PlayerConnectionServiceController;
import com.clemble.casino.server.goal.controller.GoalServiceController;
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;
import com.clemble.casino.server.payment.controller.PlayerAccountServiceController;
import com.clemble.casino.server.presence.controller.PlayerPresenceServiceController;
import com.clemble.casino.server.profile.controller.PlayerImageServiceController;
import com.clemble.casino.server.profile.controller.PlayerProfileServiceController;
import com.clemble.casino.utils.ClembleConsumerDetailUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IntegrationClembleCasinoRegistrationOperations implements ClembleCasinoRegistrationOperations {

    final private String host;
    final private ObjectMapper objectMapper;
    final private PlayerFacadeRegistrationService registrationService;
    final private PlayerProfileServiceController profileOperations;
    final private PlayerImageServiceController imageService;
    final private PlayerConnectionServiceController connectionService;
    final private PlayerPresenceServiceController presenceService;
    final private PlayerSessionServiceController sessionOperations;
    final private PlayerAccountServiceController paymentService;
    final private PaymentTransactionServiceController paymentTransactionService;
    final private EventListenerOperationsFactory listenerOperations;
    final private AutoGameConstructionController gameConstructionService;
    final private AvailabilityGameConstructionController availabilityConstructionService;
    final private GameInitiationServiceController initiationService;
    final private GameConfigurationService specificationService;
    final private GameActionService actionService;
    final private GameRecordService recordService;
    final private IntegrationGoalOperationsFactory goalOperationsFactory;

    public IntegrationClembleCasinoRegistrationOperations(
        String host,
        ObjectMapper objectMapper,
        EventListenerOperationsFactory listenerOperations,
        PlayerFacadeRegistrationService registrationService,
        PlayerProfileServiceController profileOperations,
        PlayerImageServiceController imageService,
        PlayerConnectionServiceController connectionService,
        PlayerSessionServiceController sessionOperations,
        PlayerAccountServiceController accountOperations,
        PaymentTransactionServiceController paymentTransactionService,
        PlayerPresenceServiceController presenceService,
        AutoGameConstructionController gameConstructionService,
        AvailabilityGameConstructionController availabilityConstructionService,
        GameInitiationServiceController initiationService,
        GameConfigurationService specificationService,
        GameActionService actionService,
        GameRecordService recordService,
        IntegrationGoalOperationsFactory goalOperationsFactory) {
        this.host = checkNotNull(host);
        this.objectMapper = checkNotNull(objectMapper);
        this.registrationService = checkNotNull(registrationService);
        this.listenerOperations = checkNotNull(listenerOperations);
        this.sessionOperations = checkNotNull(sessionOperations);
        this.profileOperations = checkNotNull(profileOperations);
        this.imageService = checkNotNull(imageService);
        this.connectionService = checkNotNull(connectionService);
        this.availabilityConstructionService = checkNotNull(availabilityConstructionService);
        this.paymentService = checkNotNull(accountOperations);
        this.paymentTransactionService = checkNotNull(paymentTransactionService);
        this.initiationService = checkNotNull(initiationService);
        this.presenceService = checkNotNull(presenceService);
        this.gameConstructionService = checkNotNull(gameConstructionService);
        this.specificationService = checkNotNull(specificationService);
        this.actionService = checkNotNull(actionService);
        this.recordService = checkNotNull(recordService);
        this.goalOperationsFactory = checkNotNull(goalOperationsFactory);
    }

    @Override
    public ClembleCasinoOperations login(PlayerCredential playerCredentials) {
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        PlayerLoginRequest loginRequest = new PlayerLoginRequest(consumerDetails, playerCredentials);
        PlayerToken token = registrationService.login(loginRequest);
        return create(token, playerCredentials);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        PlayerRegistrationRequest loginRequest = new PlayerRegistrationRequest(consumerDetails, playerCredential, playerProfile);
        PlayerToken token = registrationService.createPlayer(loginRequest);
        return create(token, playerCredential);
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        PlayerSocialRegistrationRequest loginRequest = new PlayerSocialRegistrationRequest(consumerDetails, playerCredential, socialConnectionData);
        PlayerToken token = registrationService.createSocialPlayer(loginRequest);
        return create(token, playerCredential);
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialAccessGrant accessGrant) {
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        PlayerSocialGrantRegistrationRequest loginRequest = new PlayerSocialGrantRegistrationRequest(consumerDetails, playerCredential, accessGrant);
        PlayerToken token = registrationService.createSocialGrantPlayer(loginRequest);
        return create(token, playerCredential);
    }

    private ClembleCasinoOperations create(PlayerToken playerIdentity, PlayerCredential credential) {
        return new IntegrationClembleCasinoOperations(
            host,
            objectMapper,
            playerIdentity,
            credential,
            profileOperations,
            imageService,
            connectionService,
            sessionOperations,
            paymentService,
            paymentTransactionService,
            listenerOperations,
            presenceService,
            gameConstructionService,
            availabilityConstructionService,
            initiationService,
            specificationService,
            actionService,
            recordService,
            goalOperationsFactory.construct(playerIdentity.getPlayer())
        );
    }

}
