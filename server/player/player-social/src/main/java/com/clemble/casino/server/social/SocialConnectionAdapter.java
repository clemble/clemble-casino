package com.clemble.casino.server.social;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.social.ApiBinding;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionKey;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;

import java.util.Collection;

public interface SocialConnectionAdapter<A extends ApiBinding> {

    ConnectionFactory<A> getConnectionFactory();

    PlayerProfile fetchPlayerProfile(A api);

    String getEmail(A api);

    Collection<ConnectionKey> fetchConnections(A api);

    Pair<String, String> toImageUrl(Connection<A> connectionKey);

    ConnectionData toConnectionData(SocialAccessGrant accessGrant);

    ConnectionData toConnectionData(SocialConnectionData connectionData);

    default ConnectionKey toConnectionKey(String id) {
        return new ConnectionKey(getConnectionFactory().getProviderId(), id);
    }

}
