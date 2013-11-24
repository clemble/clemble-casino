package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.game.GameConstructionOperations;
import com.clemble.casino.integration.event.EventListenerOperationsFactory;
import com.clemble.casino.payment.service.PaymentService;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.client.ClientDetails;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.service.PlayerPresenceService;
import com.clemble.casino.player.service.PlayerProfileService;
import com.clemble.casino.player.service.PlayerRegistrationService;
import com.clemble.casino.player.service.PlayerSessionService;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.test.random.ObjectGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerPlayerOperations implements PlayerOperations, ApplicationContextAware {

    final private ObjectMapper objectMapper;
    final private PlayerRegistrationService registrationService;
    final private PlayerProfileService profileOperations;
    final private PlayerPresenceService presenceService;
    final private PlayerSessionService sessionOperations;
    final private PaymentService paymentService;
    final private EventListenerOperationsFactory listenerOperations;
    final private Set<GameConstructionOperations<?>> gameConstructionOperations = new HashSet<>();

    public ServerPlayerOperations(ObjectMapper objectMapper,
            EventListenerOperationsFactory listenerOperations,
            PlayerRegistrationService registrationService,
            PlayerProfileService profileOperations,
            PlayerSessionService sessionOperations,
            PaymentService accountOperations,
            PlayerPresenceService presenceService) {
        this.objectMapper = checkNotNull(objectMapper);
        this.registrationService = checkNotNull(registrationService);
        this.listenerOperations = checkNotNull(listenerOperations);
        this.sessionOperations = checkNotNull(sessionOperations);
        this.profileOperations = checkNotNull(profileOperations);
        this.paymentService = checkNotNull(accountOperations);
        this.presenceService = checkNotNull(presenceService);
    }

    @Override
    public ClembleCasinoOperations createPlayer(PlayerRegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerToken playerIdentity = registrationService.createPlayer(registrationRequest);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        return create(playerIdentity, registrationRequest.getPlayerCredential());

    }

    @Override
    public ClembleCasinoOperations login(PlayerLoginRequest credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerToken playerIdentity = registrationService.login(credential);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        return create(playerIdentity, credential.getPlayerCredential());
    }

    @Override
    @SuppressWarnings("unchecked")
    final public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        gameConstructionOperations.addAll((Collection<? extends GameConstructionOperations<?>>) (Collection<?>) applicationContext.getBeansOfType(GameConstructionOperations.class).values());
    }

    @Override
    final public ClembleCasinoOperations createPlayer() {
        return createPlayer(new NativePlayerProfile().setFirstName(RandomStringUtils.randomAlphabetic(10)).setLastName(RandomStringUtils.randomAlphabetic(10))
                .setNickName(RandomStringUtils.randomAlphabetic(10)));
    }

    @Override
    final public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile) {
        // Step 0. Sanity check
        checkNotNull(playerProfile);
        // Step 1. Creating RegistrationRequest for processing
        PlayerCredential playerCredential = new PlayerCredential().setEmail(RandomStringUtils.randomAlphabetic(30) + "@gmail.com").setPassword(
                UUID.randomUUID().toString());

        ClembleConsumerDetails consumerDetails = new ClembleConsumerDetails(UUID.randomUUID().toString(), "IT", ObjectGenerator.generate(RSAKeySecret.class),
                null, new ClientDetails("IT"));
        PlayerRegistrationRequest registrationRequest = new PlayerRegistrationRequest(playerProfile, playerCredential, consumerDetails);
        // Step 2. Forwarding to appropriate method for processing
        return createPlayer(registrationRequest);
    }

    final public ClembleCasinoOperations create(PlayerToken playerIdentity, PlayerCredential credential) {
        return new ServerPlayer(objectMapper, playerIdentity, credential, profileOperations, sessionOperations, paymentService, listenerOperations,
                presenceService, gameConstructionOperations);
    }

}
