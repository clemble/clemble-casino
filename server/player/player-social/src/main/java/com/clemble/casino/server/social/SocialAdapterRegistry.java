package com.clemble.casino.server.social;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import com.clemble.casino.error.ClembleCasinoError;
import com.clemble.casino.error.ClembleCasinoException;
import com.clemble.casino.player.PlayerAware;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.social.ApiBinding;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;

public class SocialAdapterRegistry {

    final private EnumMap<SocialProvider, SocialAdapter<?>> ADAPTERS_MAP = new EnumMap<SocialProvider, SocialAdapter<?>>(SocialProvider.class);

    public SocialAdapterRegistry() {
    }

    public SocialAdapter<?> register(SocialAdapter<?> socialAdapter) {
        SocialProvider provider = SocialProvider.valueOf(socialAdapter.getConnectionFactory().getProviderId());
        return ADAPTERS_MAP.put(provider, socialAdapter);
    }

    public SocialAdapter<?> getSocialAdapter(SocialProvider provider) {
        // Step 1. Fetching SocialConnectionAdapter
        SocialAdapter<?> connectionAdapter = ADAPTERS_MAP.get(provider);
        // Step 2. Sanity check
        if (connectionAdapter == null)
            throw ClembleCasinoException.fromError(ClembleCasinoError.SocialConnectionProviderNotSupported, PlayerAware.DEFAULT_PLAYER, provider.name());
        // Step 3. Returning found ConnectionAdapters
        return connectionAdapter;
    }

}
