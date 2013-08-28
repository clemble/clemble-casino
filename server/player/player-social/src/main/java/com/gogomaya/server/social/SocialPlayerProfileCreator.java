package com.gogomaya.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.player.registration.PlayerRegistrationService;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.web.RegistrationRequest;

public class SocialPlayerProfileCreator implements ConnectionSignUp {

    final private PlayerRegistrationService playerRegistrationService;

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;

    @Inject
    public SocialPlayerProfileCreator(
            final PlayerRegistrationService playerRegistrationService,
            final SocialConnectionAdapterRegistry socialAdapterRegistry) {
        this.playerRegistrationService = checkNotNull(playerRegistrationService);
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String execute(Connection<?> connection) {
        // Step 1. Checking provided Connection<?> valid
        if (connection == null)
            throw new IllegalArgumentException("No Connection defined.");
        // Step 2. Retrieving SocialAdapter for the Connection
        ConnectionKey connectionKey = connection.getKey();
        SocialConnectionAdapter socialAdapter = socialAdapterRegistry.getSocialAdapter(connectionKey.getProviderId());
        if (socialAdapter == null)
            throw new IllegalArgumentException("No SocialAdapter exists for Connection");
        // Step 3. Generating gamer profile based on SocialConnection
        PlayerProfile playerProfile = socialAdapter.fetchGamerProfile(connection.getApi());
        PlayerCredential playerCredential = new PlayerCredential()
            .setEmail(connectionKey.getProviderUserId() + "@" + connectionKey.getProviderId())
            .setPassword(UUID.randomUUID().toString());
        RegistrationRequest playerRegistration = new RegistrationRequest()
            .setPlayerProfile(playerProfile)
            .setPlayerCredential(playerCredential);
        // Step 4. Performing actual registration
        PlayerIdentity playerIdentity = playerRegistrationService.register(playerRegistration);
        // Step 5. Returning playerIdentity as a result
        return String.valueOf(playerIdentity.getPlayerId());
    }

}
