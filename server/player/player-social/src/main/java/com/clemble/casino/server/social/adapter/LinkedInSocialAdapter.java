package com.clemble.casino.server.social.adapter;

import java.util.List;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

import com.clemble.casino.player.PlayerCategory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.player.PlayerSocialNetwork;
import com.clemble.casino.server.social.SocialConnectionAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: mavarazy
 * Date: 09/12/13
 * Time: 18:16
 * To change this template use File | Settings | File Templates.
 */
public class LinkedInSocialAdapter extends SocialConnectionAdapter<LinkedIn> {

    final private LinkedInConnectionFactory linkedInConnectionFactory;

    public LinkedInSocialAdapter(LinkedInConnectionFactory connectionFactory) {
        super("linkedin");
        this.linkedInConnectionFactory = connectionFactory;
    }

    @Override
    public PlayerProfile fetchPlayerProfile(LinkedIn api) {
        // Step 1. Fetching LinkedInProfileFull
        LinkedInProfileFull linkedInProfile = api.profileOperations().getUserProfileFull();
        // Step 2. Generating SocialPlayerProfile
        return new PlayerProfile()
            .addSocialConnection(toConnectionKey(linkedInProfile.getId()))
            .setFirstName(linkedInProfile.getFirstName())
            .setLastName(linkedInProfile.getLastName())
            .setNickName(linkedInProfile.getFirstName())
            .setImageUrl(linkedInProfile.getProfilePictureUrl())
            .setCategory(PlayerCategory.Novice);
    }

    @Override
    public PlayerSocialNetwork fetchPlayerNetwork(PlayerProfile playerProfile, LinkedIn api) {
        // Step 1. Creating owned social network
        PlayerSocialNetwork socialNetwork = new PlayerSocialNetwork()
            .addOwned(playerProfile.getSocialConnection(getProviderId()))
            .setPlayer(playerProfile.getPlayer());;
        // Step 2. Fetching all friend connections
        int position = 0;
        List<LinkedInProfile> colleagues = api.connectionOperations().getConnections(position, 500);
        do {
            colleagues = api.connectionOperations().getConnections(position, 500);
            for(LinkedInProfile linkedInProfile: colleagues)
                socialNetwork.addConnection(toConnectionKey(linkedInProfile.getId()));
            position += 500;
        } while(colleagues.size() == 500);
        // Step 3. Returning created PlayerProfile
        return socialNetwork;
    }

    @Override
    public ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        Connection<LinkedIn> linkedInConnection = linkedInConnectionFactory.createConnection(accessGrant.toOAuthToken());
        return linkedInConnection.createData();
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