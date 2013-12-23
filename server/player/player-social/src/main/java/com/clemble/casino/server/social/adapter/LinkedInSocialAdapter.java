package com.clemble.casino.server.social.adapter;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

import com.clemble.casino.player.PlayerCategory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.PlayerProfile;
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
    public PlayerProfile fetchGamerProfile(LinkedIn api) {
        // Step 1. Fetching LinkedInProfileFull
        LinkedInProfileFull linkedInProfile = api.profileOperations().getUserProfileFull();
        // Step 2. Generating SocialPlayerProfile
        return new PlayerProfile()
            .addSocialConnection(new ConnectionKey("linkedin", linkedInProfile.getId()))
            .setFirstName(linkedInProfile.getFirstName())
            .setLastName(linkedInProfile.getLastName())
            .setNickName(linkedInProfile.getFirstName())
            .setImageUrl(linkedInProfile.getProfilePictureUrl())
            .setCategory(PlayerCategory.Novice);
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
