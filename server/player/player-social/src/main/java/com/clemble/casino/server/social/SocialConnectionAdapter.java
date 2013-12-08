package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.oauth2.AccessGrant;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;

public abstract class SocialConnectionAdapter<A> {

    final private String providerId;

    protected SocialConnectionAdapter(final String provider) {
        this.providerId = checkNotNull(provider);
    }

    public String getProviderId() {
        return providerId;
    }

    abstract public PlayerProfile fetchGamerProfile(A api);

    abstract public ConnectionData toConnectionData(SocialAccessGrant accessGrant);

    abstract public ConnectionData toConnectionData(SocialConnectionData connectionData);

}
