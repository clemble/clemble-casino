package com.clemble.casino.server.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.player.social.PlayerSocialNetwork;

public abstract class SocialConnectionAdapter<A> {

    final private String providerId;

    protected SocialConnectionAdapter(final String provider) {
        this.providerId = checkNotNull(provider);
    }

    public String getProviderId() {
        return providerId;
    }

    public ConnectionKey toConnectionKey(String id) {
        return new ConnectionKey(providerId, id);
    }

    abstract public PlayerProfile fetchPlayerProfile(A api);

    abstract public ConnectionData toConnectionData(SocialAccessGrant accessGrant);

    abstract public ConnectionData toConnectionData(SocialConnectionData connectionData);

    abstract public PlayerSocialNetwork enrichPlayerNetwork(PlayerSocialNetwork socialNetwork, A api);

}
