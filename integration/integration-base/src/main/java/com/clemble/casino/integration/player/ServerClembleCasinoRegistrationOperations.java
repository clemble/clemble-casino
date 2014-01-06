package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.game.service.GameActionService;
import com.clemble.casino.game.service.GameConstructionService;
import com.clemble.casino.game.service.GameSpecificationService;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerConnectionService;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialGrantRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;
import com.clemble.casino.utils.ClembleConsumerDetailUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerClembleCasinoRegistrationOperations implements ClembleCasinoRegistrationOperations {

    final private ObjectMapper objectMapper;
    final private PlayerRegistrationService registrationService;
    final private PlayerProfileService profileOperations;
    final private PlayerConnectionService connectionService;
    final private PlayerPresenceService presenceService;
    final private PlayerSessionService sessionOperations;
    final private PaymentService paymentService;
    final private EventListenerOperationsFactory listenerOperations;
    final private GameConstructionService gameConstructionService;
    final private GameSpecificationService specificationService;
    final private GameActionService<?> actionService;

    public ServerClembleCasinoRegistrationOperations(ObjectMapper objectMapper,
            EventListenerOperationsFactory listenerOperations,
            PlayerRegistrationService registrationService,
            PlayerProfileService profileOperations,
            PlayerConnectionService connectionService,
            PlayerSessionService sessionOperations,
            PaymentService accountOperations,
            PlayerPresenceService presenceService,
            GameConstructionService gameConstructionService,
            GameSpecificationService specificationService,
            GameActionService<?> actionService) {
        this.objectMapper = checkNotNull(objectMapper);
        this.registrationService = checkNotNull(registrationService);
        this.listenerOperations = checkNotNull(listenerOperations);
        this.sessionOperations = checkNotNull(sessionOperations);
        this.profileOperations = checkNotNull(profileOperations);
        this.connectionService = checkNotNull(connectionService);
        this.paymentService = checkNotNull(accountOperations);
        this.presenceService = checkNotNull(presenceService);
        this.gameConstructionService = checkNotNull(gameConstructionService);
        this.specificationService = checkNotNull(specificationService);
        this.actionService = checkNotNull(actionService);
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
        return new ServerPlayer(objectMapper, playerIdentity, credential, profileOperations, connectionService, sessionOperations, paymentService, listenerOperations, presenceService, gameConstructionService, specificationService, actionService);
    }

}