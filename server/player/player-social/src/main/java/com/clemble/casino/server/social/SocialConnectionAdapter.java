package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;

import java.util.Collection;

public abstract class SocialConnectionAdapter<A> {

    final private String providerId;

    // TODO forbid email related to the social to be recreated as a separate user
    // For example FB account A has email abc@cba.com, don't allow to create user with email abc@cba.com

    protected SocialConnectionAdapter(final String provider) {
        this.providerId = checkNotNull(provider);
    }

    public String getProviderId() {
        return providerId;
    }

    abstract public PlayerProfile fetchPlayerProfile(A api);

    abstract public Collection<ConnectionKey> fetchConnections(A api);

    abstract public Pair<String, String> toImageUrl(Connection<A> connectionKey);

    abstract public ConnectionData toConnectionData(SocialAccessGrant accessGrant);

    abstract public ConnectionData toConnectionData(SocialConnectionData connectionData);

    public ConnectionKey toConnectionKey(String id) {
        return new ConnectionKey(providerId, id);
    }

}
