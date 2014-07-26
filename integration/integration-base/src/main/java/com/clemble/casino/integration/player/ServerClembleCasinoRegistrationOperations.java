package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.game.service.AvailabilityGameConstructionService;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.AutoGameConstructionService;
import com.clemble.casino.game.service.GameInitiationService;
import com.clemble.casino.game.service.GameConfigurationService;
import com.clemble.casino.game.service.GameRecordService;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.*;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;
import com.clemble.casino.utils.ClembleConsumerDetailUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerClembleCasinoRegistrationOperations implements ClembleCasinoRegistrationOperations {

    final private String host;
    final private ObjectMapper objectMapper;
    final private PlayerFacadeRegistrationService registrationService;
    final private PlayerProfileService profileOperations;
    final private PlayerImageService imageService;
    final private PlayerConnectionService connectionService;
    final private PlayerPresenceService presenceService;
    final private PlayerSessionService sessionOperations;
    final private PaymentService paymentService;
    final private EventListenerOperationsFactory listenerOperations;
    final private AutoGameConstructionService gameConstructionService;
    final private AvailabilityGameConstructionService availabilityConstructionService;
    final private GameInitiationService initiationService;
    final private GameConfigurationService specificationService;
    final private GameActionService actionService;
    final private GameRecordService recordService;

    public ServerClembleCasinoRegistrationOperations(
        String host,
        ObjectMapper objectMapper,
        EventListenerOperationsFactory listenerOperations,
        PlayerFacadeRegistrationService registrationService,
        PlayerProfileService profileOperations,
        PlayerImageService imageService,
        PlayerConnectionService connectionService,
        PlayerSessionService sessionOperations,
        PaymentService accountOperations,
        PlayerPresenceService presenceService,
        AutoGameConstructionService gameConstructionService,
        AvailabilityGameConstructionService availabilityConstructionService,
        GameInitiationService initiationService,
        GameConfigurationService specificationService,
        GameActionService actionService,
        GameRecordService recordService) {
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
        this.initiationService = checkNotNull(initiationService);
        this.presenceService = checkNotNull(presenceService);
        this.gameConstructionService = checkNotNull(gameConstructionService);
        this.specificationService = checkNotNull(specificationService);
        this.actionService = checkNotNull(actionService);
        this.recordService = checkNotNull(recordService);
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
        PlayerRegistrationRequest loginRequest = new PlayerRegistrationRequest(playerProfile, playerCredential, consumerDetails);
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
        return new ServerCasinoOperations(host, objectMapper, playerIdentity, credential, profileOperations, imageService, connectionService, sessionOperations, paymentService, listenerOperations, presenceService, gameConstructionService, availabilityConstructionService, initiationService, specificationService, actionService, recordService);
    }

}
