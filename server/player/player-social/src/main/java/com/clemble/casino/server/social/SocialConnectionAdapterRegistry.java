package com.clemble.casino.server.social;

import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;

public class SocialConnectionAdapterRegistry {

    final private Map<String, SocialConnectionAdapter<?>> ADAPTERS_MAP = new HashMap<String, SocialConnectionAdapter<?>>();

    public SocialConnectionAdapterRegistry() {
    }

    public SocialConnectionAdapter<?> register(SocialConnectionAdapter<?> socialAdapter) {
        return ADAPTERS_MAP.put(socialAdapter.getProviderId(), socialAdapter);
    }

    public SocialConnectionAdapter<?> getSocialAdapter(String providerId) {
        // Step 1. Fetching SocialConnectionAdapter
        SocialConnectionAdapter<?> connectionAdapter = ADAPTERS_MAP.get(providerId);
        // Step 2. Sanity check
        if (connectionAdapter == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionProviderNotSupported);
        // Step 3. Returning found ConnectionAdapters
        return connectionAdapter;
    }

}
