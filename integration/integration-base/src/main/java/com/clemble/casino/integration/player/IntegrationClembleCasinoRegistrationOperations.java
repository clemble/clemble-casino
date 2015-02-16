package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.game.lifecycle.configuration.service.GameConfigurationService;
import com.clemble.casino.game.lifecycle.record.service.GameRecordService;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.integration.goal.IntegrationGoalOperationsFactory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.service.PlayerPasswordResetService;
import com.clemble.casino.server.connection.controller.PlayerFriendInvitationServiceController;
import com.clemble.casino.server.email.controller.PlayerEmailServiceController;
import com.clemble.casino.server.game.construction.controller.AutoGameConstructionController;
import com.clemble.casino.server.game.construction.controller.AvailabilityGameConstructionController;
import com.clemble.casino.server.game.construction.controller.GameInitiationServiceController;
import com.clemble.casino.server.game.controller.GameActionServiceController;
import com.clemble.casino.server.post.controller.PlayerFeedServiceController;
import com.clemble.casino.server.presence.controller.PlayerSessionServiceController;
import com.clemble.casino.server.registration.controller.PlayerPasswordResetServiceController;
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
import com.clemble.casino.server.payment.controller.PaymentTransactionServiceController;
import com.clemble.casino.server.payment.controller.PlayerAccountServiceController;
import com.clemble.casino.server.presence.controller.PlayerPresenceServiceController;
import com.clemble.casino.server.profile.controller.PlayerImageServiceController;
import com.clemble.casino.server.profile.controller.PlayerProfileServiceController;
import com.clemble.casino.utils.ClembleConsumerDetailUtils;
import com.clemble.server.tag.controller.PlayerTagServiceController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.clemble.casino.server.notification.controller.PlayerNotificationServiceController;

public class IntegrationClembleCasinoRegistrationOperations implements ClembleCasinoRegistrationOperations {

    final private String host;
    final private ObjectMapper objectMapper;
    final private PlayerFacadeRegistrationService registrationService;
    final private PlayerProfileServiceController profileOperations;
    final private PlayerImageServiceController imageService;
    final private PlayerConnectionServiceController connectionService;
    final private PlayerFriendInvitationServiceController invitationService;
    final private PlayerPresenceServiceController presenceService;
    final private PlayerSessionServiceController sessionOperations;
    final private PlayerAccountServiceController paymentService;
    final private PaymentTransactionServiceController paymentTransactionService;
    final private EventListenerOperationsFactory listenerOperations;
    final private AutoGameConstructionController gameConstructionService;
    final private AvailabilityGameConstructionController availabilityConstructionService;
    final private GameInitiationServiceController initiationService;
    final private GameConfigurationService specificationService;
    final private GameActionServiceController actionService;
    final private GameRecordService recordService;
    final private IntegrationGoalOperationsFactory goalOperationsFactory;
    final private PlayerNotificationServiceController notificationServiceController;
    final private PlayerFeedServiceController feedServiceController;
    final private PlayerPasswordResetServiceController passwordResetService;
    final private PlayerEmailServiceController emailServiceController;
    final private PlayerTagServiceController tagServiceController;

    public IntegrationClembleCasinoRegistrationOperations(
        String host,
        ObjectMapper objectMapper,
        EventListenerOperationsFactory listenerOperations,
        PlayerFacadeRegistrationService registrationService,
        PlayerProfileServiceController profileOperations,
        PlayerImageServiceController imageService,
        PlayerConnectionServiceController connectionService,
        PlayerFriendInvitationServiceController invitationService,
        PlayerSessionServiceController sessionOperations,
        PlayerAccountServiceController accountOperations,
        PaymentTransactionServiceController paymentTransactionService,
        PlayerPresenceServiceController presenceService,
        AutoGameConstructionController gameConstructionService,
        AvailabilityGameConstructionController availabilityConstructionService,
        GameInitiationServiceController initiationService,
        GameConfigurationService specificationService,
        GameActionServiceController actionService,
        GameRecordService recordService,
        IntegrationGoalOperationsFactory goalOperationsFactory,
        PlayerNotificationServiceController notificationServiceController,
        PlayerFeedServiceController feedServiceController,
        PlayerPasswordResetServiceController passwordResetService,
        PlayerEmailServiceController emailServiceController,
        PlayerTagServiceController tagServiceController) {
        this.host = checkNotNull(host);
        this.objectMapper = checkNotNull(objectMapper);
        this.registrationService = checkNotNull(registrationService);
        this.listenerOperations = checkNotNull(listenerOperations);
        this.sessionOperations = checkNotNull(sessionOperations);
        this.profileOperations = checkNotNull(profileOperations);
        this.imageService = checkNotNull(imageService);
        this.connectionService = checkNotNull(connectionService);
        this.invitationService = checkNotNull(invitationService);
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
        this.notificationServiceController = checkNotNull(notificationServiceController);
        this.feedServiceController = feedServiceController;
        this.passwordResetService = passwordResetService;
        this.emailServiceController = emailServiceController;
        this.tagServiceController = tagServiceController;
    }

    @Override
    public ClembleCasinoOperations login(PlayerCredential playerCredentials) {
        PlayerLoginRequest loginRequest = new PlayerLoginRequest(playerCredentials);
        String player = registrationService.login(loginRequest);
        return create(player, playerCredentials);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerCredential playerCredential, PlayerProfile playerProfile) {
        PlayerRegistrationRequest loginRequest = new PlayerRegistrationRequest(playerCredential, playerProfile);
        String player = registrationService.createPlayer(loginRequest);
        return create(player, playerCredential);
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialConnectionData socialConnectionData) {
        PlayerSocialRegistrationRequest loginRequest = new PlayerSocialRegistrationRequest(playerCredential, socialConnectionData);
        String player = registrationService.createSocialPlayer(loginRequest);
        return create(player, playerCredential);
    }

    @Override
    public ClembleCasinoOperations createSocialPlayer(PlayerCredential playerCredential, SocialAccessGrant accessGrant) {
        ClembleConsumerDetails consumerDetails = ClembleConsumerDetailUtils.generateDetails();
        PlayerSocialGrantRegistrationRequest loginRequest = new PlayerSocialGrantRegistrationRequest(playerCredential, accessGrant);
        String token = registrationService.createSocialGrantPlayer(loginRequest);
        return create(token, playerCredential);
    }

    private ClembleCasinoOperations create(String player, PlayerCredential credential) {
        return new IntegrationClembleCasinoOperations(
            host,
            objectMapper,
            player,
            credential,
            profileOperations,
            imageService,
            connectionService,
            invitationService,
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
            goalOperationsFactory.construct(player),
            notificationServiceController,
            feedServiceController,
            passwordResetService,
            emailServiceController,
            tagServiceController
        );
    }

}
