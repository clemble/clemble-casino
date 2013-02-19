package com.gogomaya.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerIdentityRepository;

public class SocialPlayerProfileCreator implements ConnectionSignUp {

    final private PlayerProfileRepository playerProfileRepository;
    
    final private PlayerIdentityRepository playerIdentityRepository;

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;

    @Inject
    public SocialPlayerProfileCreator(final PlayerProfileRepository playerProfileRepository, final PlayerIdentityRepository playerIdentityRepository, final SocialConnectionAdapterRegistry socialAdapterRegistry) {
        this.playerProfileRepository = checkNotNull(playerProfileRepository);
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
        this.playerIdentityRepository = checkNotNull(playerIdentityRepository);
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String execute(Connection<?> connection) {
        // Step 1. Checking provided Connection<?> valid
        if (connection == null)
            throw new IllegalArgumentException("No Connection defined.");
        // Step 2. Retrieving SocialAdapter for the Connection
        SocialConnectionAdapter socialAdapter = socialAdapterRegistry.getSocialAdapter(connection.getKey().getProviderId());
        if (socialAdapter == null)
            throw new IllegalArgumentException("No SocialAdapter exists for Connection");
        // Step 3. Generating gamer profile based on SocialConnection
        PlayerProfile playerProfile = socialAdapter.fetchGamerProfile(connection.getApi());
        playerProfile = playerProfileRepository.saveAndFlush(playerProfile);
        // Step 4. Creating player identity
        PlayerIdentity playerIdentity = new PlayerIdentity().setPlayerId(playerProfile.getPlayerId()).setSecret(UUID.randomUUID().toString());
        playerIdentityRepository.saveAndFlush(playerIdentity);
        // Step 5. Returning String long presentation
        return String.valueOf(playerProfile.getPlayerId());
    }

}
