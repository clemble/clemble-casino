package com.clemble.casino.server.social.adapter;

import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.social.SocialConnectionAdapter;
import com.clemble.casino.social.SocialAccessGrant;
import com.clemble.casino.social.SocialConnectionData;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by mavarazy on 11/30/14.
 */
public class GoogleSocialAdapter extends SocialConnectionAdapter<Google>{

    final private GoogleConnectionFactory googleFactory;

    public GoogleSocialAdapter(GoogleConnectionFactory googleFactory) {
        super("google");
        this.googleFactory = googleFactory;
    }

    @Override
    public PlayerProfile fetchPlayerProfile(Google api) {
        // Step 1. Fetching google profile
        Person profile = api.plusOperations().getGoogleProfile();
        // Step 2. Converting google profile to Player Profile
        return new PlayerProfile().
            addSocialConnection(new ConnectionKey("google", profile.getId())).
            setBirthDate(profile.getBirthday()).
            setFirstName(profile.getGivenName()).
            setGender(PlayerGender.parse(profile.getGender())).
            setLastName(profile.getFamilyName()).
            setNickName(profile.getDisplayName());
    }

    @Override
    public Collection<ConnectionKey> fetchConnections(Google api) {
        return Collections.emptyList();
    }

    @Override
    public Pair<String, String> toImageUrl(Connection<Google> connection) {
        // Step 1. Fetching API's
        Google api = connection.getApi();
        // Step 2. Fetching image URL
        Person profile = api.plusOperations().getGoogleProfile();
        String imageUrl = profile.getImageUrl();
        return new ImmutablePair<String, String>(imageUrl, imageUrl);
    }

    @Override
    public ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        Connection<Google> google = googleFactory.createConnection(accessGrant.toAccessGrant());
        return google.createData();
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
