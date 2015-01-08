package com.clemble.casino.server.social;

import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerAware;

public class SocialConnectionAdapterRegistry {

    final private Map<String, SocialAdapter<?>> ADAPTERS_MAP = new HashMap<String, SocialAdapter<?>>();

    public SocialConnectionAdapterRegistry() {
    }

    public SocialAdapter<?> register(SocialAdapter<?> socialAdapter) {
        return ADAPTERS_MAP.put(socialAdapter.getConnectionFactory().getProviderId(), socialAdapter);
    }

    public SocialAdapter<?> getSocialAdapter(String providerId) {
        // Step 1. Fetching SocialConnectionAdapter
        SocialAdapter<?> connectionAdapter = ADAPTERS_MAP.get(providerId);
        // Step 2. Sanity check
        if (connectionAdapter == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionProviderNotSupported, PlayerAware.DEFAULT_PLAYER, providerId);
        // Step 3. Returning found ConnectionAdapters
        return connectionAdapter;
    }

}
