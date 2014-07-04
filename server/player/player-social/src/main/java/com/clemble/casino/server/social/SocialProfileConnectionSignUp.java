package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;

public class SocialProfileConnectionSignUp implements ConnectionSignUp {

    final private SocialConnectionAdapterRegistry socialAdapterRegistry;

    public SocialProfileConnectionSignUp(final SocialConnectionAdapterRegistry socialAdapterRegistry) {
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
        return connectionKey.getProviderId() + ":" + connectionKey.getProviderUserId();
    }

}
