package com.gogomaya.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.ConnectionData;

import com.gogomaya.server.user.GamerProfile;
import com.gogomaya.server.user.SocialConnectionData;

public abstract class SocialConnectionAdapter<A> {

    final private String providerId;

    protected SocialConnectionAdapter(final String provider) {
        this.providerId = checkNotNull(provider);
    }

    public String getProviderId() {
        return providerId;
    }

    abstract public GamerProfile fetchGamerProfile(A api);

    abstract public ConnectionData toConnectionData(SocialConnectionData connectionData);

}
