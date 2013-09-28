package com.gogomaya.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;

import com.gogomaya.player.PlayerProfile;

public class SocialPlayerProfileCreator implements ConnectionSignUp {

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;

    public SocialPlayerProfileCreator(final SocialConnectionAdapterRegistry socialAdapterRegistry) {
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
        // Step 4. Returning playerIdentity as a result
        return String.valueOf(playerProfile.getPlayer());
    }

}
