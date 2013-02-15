package com.gogomaya.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

import com.gogomaya.server.user.GamerProfile;
import com.gogomaya.server.user.GamerProfileRepository;

public class SocialGamerProfileCreator implements ConnectionSignUp {

    final private GamerProfileRepository gamerProfileRepository;

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;

    @Inject
    public SocialGamerProfileCreator(final GamerProfileRepository gamerProfileRepository, final SocialConnectionAdapterRegistry socialAdapterRegistry) {
        this.gamerProfileRepository = checkNotNull(gamerProfileRepository);
        this.socialAdapterRegistry = checkNotNull(socialAdapterRegistry);
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
        GamerProfile gamerProfile = socialAdapter.fetchGamerProfile(connection.getApi());
        gamerProfile = gamerProfileRepository.saveAndFlush(gamerProfile);
        // Step 4. Returning user identifier
        return String.valueOf(gamerProfile.getUserId());
    }

}
