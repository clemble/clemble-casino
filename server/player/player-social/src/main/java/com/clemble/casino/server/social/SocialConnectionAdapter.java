package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;

import java.util.Collection;

public abstract class SocialConnectionAdapter<A> {

    final private String providerId;

    protected SocialConnectionAdapter(final String provider) {
        this.providerId = checkNotNull(provider);
    }

    public String getProviderId() {
        return providerId;
    }

    abstract public PlayerProfile fetchPlayerProfile(A api);

    abstract public Collection<ConnectionKey> fetchConnections(A api);

    abstract public String toImageUrl(Connection<A> connectionKey);

    abstract public ConnectionData toConnectionData(SocialAccessGrant accessGrant);

    abstract public ConnectionData toConnectionData(SocialConnectionData connectionData);

    public ConnectionKey toConnectionKey(String id) {
        return new ConnectionKey(providerId, id);
    }

}
