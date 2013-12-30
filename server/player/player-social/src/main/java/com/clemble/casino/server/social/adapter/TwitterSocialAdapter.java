package com.clemble.casino.server.social.adapter;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.twitter.api.CursoredList;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.player.PlayerSocialNetwork;
import com.clemble.casino.server.social.SocialConnectionAdapter;

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
    public PlayerProfile fetchPlayerProfile(Twitter api) {
        TwitterProfile twitterProfile = api.userOperations().getUserProfile();
        return new PlayerProfile()
                .addSocialConnection(toConnectionKey(String.valueOf(twitterProfile.getId())))
                .setImageUrl(twitterProfile.getProfileImageUrl())
                .setNickName(twitterProfile.getName());  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public PlayerSocialNetwork fetchPlayerNetwork(PlayerProfile playerProfile, Twitter api) {
        // Step 1. Creating owned social network
        PlayerSocialNetwork socialNetwork = new PlayerSocialNetwork()
            .addOwned(playerProfile.getSocialConnection(getProviderId()))
            .setPlayer(playerProfile.getPlayer());;
        // Step 2. Fetching all twitter connections (5000)
        // TODO check if this ever fails
        CursoredList<Long> friends = api.friendOperations().getFriendIds();
        for(Long twitterId: friends)
            socialNetwork.addConnection(toConnectionKey(String.valueOf(twitterId)));
        // Step 3. Returning created PlayerProfile
        return socialNetwork;
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
