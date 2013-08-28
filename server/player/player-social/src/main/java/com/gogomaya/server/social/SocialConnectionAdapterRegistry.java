package com.gogomaya.server.social;

import java.util.HashMap;
import java.util.Map;

public class SocialConnectionAdapterRegistry {

    final private Map<String, SocialConnectionAdapter<?>> ADAPTERS_MAP = new HashMap<String, SocialConnectionAdapter<?>>();

    public SocialConnectionAdapterRegistry() {
    }

    public SocialConnectionAdapter<?> register(SocialConnectionAdapter<?> socialAdapter) {
        return ADAPTERS_MAP.put(socialAdapter.getProviderId(), socialAdapter);
    }

    public SocialConnectionAdapter<?> getSocialAdapter(String providerId) {
        return ADAPTERS_MAP.get(providerId);
    }

}
