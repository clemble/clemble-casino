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
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import java.util.Collection;

public interface SocialAdapter<A extends ApiBinding> {

    ConnectionFactory<A> getConnectionFactory();

    PlayerProfile fetchPlayerProfile(A api);

    String getEmail(A api);

    Collection<ConnectionKey> fetchConnections(A api);

    Pair<String, String> toImageUrl(Connection<A> connectionKey);

    default ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        ConnectionFactory<A> connectionFactory = getConnectionFactory();
        if (connectionFactory instanceof OAuth2ConnectionFactory) {
            Connection<A> connection = ((OAuth2ConnectionFactory) connectionFactory).createConnection(accessGrant.toAccessGrant());
            return connection.createData();
        } else if (connectionFactory instanceof OAuth1ConnectionFactory) {
            Connection<A> connection = ((OAuth1ConnectionFactory) connectionFactory).createConnection(accessGrant.toOAuthToken());
            return connection.createData();
        } else {
            throw new UnsupportedOperationException();
        }
    }

    ConnectionData toConnectionData(SocialConnectionData connectionData);

    default ConnectionKey toConnectionKey(String id) {
        return new ConnectionKey(getConnectionFactory().getProviderId(), id);
    }

}
