package com.clemble.casino.server.social.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
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
            ;
    }

    @Override
    public Pair<String, String> toImageUrl(Connection<LinkedIn> connection) {
        String primaryImage = connection.getApi().profileOperations().getUserProfileFull().getProfilePictureUrl();
        String smallImage = primaryImage;
        return new ImmutablePair<>(primaryImage, smallImage);
    }

    @Override
    public Collection<ConnectionKey> fetchConnections(LinkedIn api) {
        Collection<ConnectionKey> connections = new ArrayList<>();
        // Step 1. Fetching all friend connections
        int position = 0;
        List<LinkedInProfile> colleagues = api.connectionOperations().getConnections(position, 500);
        do {
            colleagues = api.connectionOperations().getConnections(position, 500);
            for(LinkedInProfile linkedInProfile: colleagues)
                connections.add(toConnectionKey(linkedInProfile.getId()));
            position += 500;
        } while(colleagues.size() == 500);
        // Step 2. Returning created PlayerProfile
        return connections;
    }

    @Override
    public ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        Connection<LinkedIn> linkedInConnection = linkedInConnectionFactory.createConnection(accessGrant.toAccessGrant());
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
