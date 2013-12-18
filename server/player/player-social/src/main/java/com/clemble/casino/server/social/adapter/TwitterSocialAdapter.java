package com.clemble.casino.server.social.adapter;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.social.SocialConnectionAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;

import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

/**
 * Created with IntelliJ IDEA.
 * User: mavarazy
 * Date: 09/12/13
 * Time: 06:55
 * To change this template use File | Settings | File Templates.
 */
public class TwitterSocialAdapter extends SocialConnectionAdapter<Twitter>{

    final private TwitterConnectionFactory twitterConnectionFactory;

    public TwitterSocialAdapter(TwitterConnectionFactory twitterConnectionFactory) {
        super("twitter");
        this.twitterConnectionFactory = twitterConnectionFactory;
    }

    @Override
    public PlayerProfile fetchGamerProfile(Twitter api) {
        TwitterProfile twitterProfile = api.userOperations().getUserProfile();
        return new PlayerProfile()
                .addSocialConnection(new ConnectionKey("twitter", String.valueOf(twitterProfile.getId())))
                .setImageUrl(twitterProfile.getProfileImageUrl())
                .setNickName(twitterProfile.getName());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        Connection<Twitter> twitterConnection = twitterConnectionFactory.createConnection(accessGrant.toOAuthToken());
        return twitterConnection.createData();
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
