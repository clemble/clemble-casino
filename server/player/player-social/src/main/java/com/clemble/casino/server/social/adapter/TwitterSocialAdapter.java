package com.clemble.casino.server.social.adapter;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import com.clemble.casino.server.social.SocialAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: mavarazy
 * Date: 09/12/13
 * Time: 06:55
 * To change this template use File | Settings | File Templates.
 */
public class TwitterSocialAdapter implements SocialAdapter<Twitter> {

    final private TwitterConnectionFactory twitterConnectionFactory;

    public TwitterSocialAdapter(TwitterConnectionFactory twitterConnectionFactory) {
        this.twitterConnectionFactory = twitterConnectionFactory;
    }

    public ConnectionFactory<Twitter> getConnectionFactory() {
        return twitterConnectionFactory;
    }

    @Override
    public PlayerProfile fetchPlayerProfile(Twitter api) {
        TwitterProfile twitterProfile = api.userOperations().getUserProfile();
        return new PlayerProfile()
                .addSocialConnection(toConnectionKey(String.valueOf(twitterProfile.getId())))
                .setNickName(twitterProfile.getName());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getEmail(Twitter twitter) {
        return null;
    }

    @Override
    public Pair<String, String> toImageUrl(Connection<Twitter> connection) {
        String primaryImage = connection.getApi().userOperations().getUserProfile().getProfileImageUrl();
        String smallImage = primaryImage + "?size=normal";
        return new ImmutablePair<>(primaryImage, smallImage);
    }

    @Override
    public Collection<ConnectionKey> fetchConnections(Twitter api) {
        Collection<ConnectionKey> connections = new ArrayList<>();
        // Step 1. Fetching all twitter connections (5000)
        // TODO check if this ever fails
        CursoredList<Long> friends = api.friendOperations().getFriendIds();
        for(Long twitterId: friends)
            connections.add(toConnectionKey(String.valueOf(twitterId)));
        // Step 2. Returning created PlayerProfile
        return connections;
    }

    @Override
    public ConnectionData toConnectionData(SocialConnectionData connectionData) {
        return new ConnectionData(connectionData.getProviderId(),
                connectionData.getProviderUserId(),
                "",
                "",
                "",
                connectionData.getAccessToken(),
                connectionData.getSecret(),
                connectionData.getRefreshToken(),
                connectionData.getExpireTime());

    }

}
