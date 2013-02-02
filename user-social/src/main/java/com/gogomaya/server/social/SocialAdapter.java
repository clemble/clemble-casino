package com.gogomaya.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import org.springframework.social.connect.ConnectionData;

import com.gogomaya.server.user.GamerProfile;
import com.gogomaya.server.user.SocialConnectionData;

abstract public class SocialAdapter<A> {

    final private String providerId;

    final private static Map<String, SocialAdapter<?>> ADAPTERS_MAP = new HashMap<String, SocialAdapter<?>>();

    protected SocialAdapter(final String provider) {
        this.providerId = checkNotNull(provider);
        ADAPTERS_MAP.put(providerId, this);
    }

    public String getProviderId() {
        return providerId;
    }

    public static SocialAdapter<?> getSocialAdapter(String providerId) {
        return ADAPTERS_MAP.get(providerId);
    }

    abstract public GamerProfile fetchGamerProfile(A api);

    abstract public ConnectionData toConnectionData(SocialConnectionData connectionData);

}
