package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.server.KeyGenerator;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.util.Base64Utils;

import java.nio.ByteBuffer;

public class PlayerSocialKeyGenerator implements ConnectionSignUp, KeyGenerator {

    public PlayerSocialKeyGenerator() {
    }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String execute(Connection<?> connection) {
        // Step 1. Checking provided Connection<?> valid
        if (connection == null)
            throw new IllegalArgumentException("No Connection defined.");
        // Step 2. Retrieving SocialAdapter for the Connection
        ConnectionKey connectionKey = connection.getKey();
        /// WARNING Changing this might cause URL encoding problems when adding more connections
        return connectionKey.getProviderId() + connectionKey.getProviderUserId();
    }

}
