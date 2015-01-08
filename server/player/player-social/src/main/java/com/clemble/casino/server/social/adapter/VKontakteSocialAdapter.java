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
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.vkontakte.api.VKontakte;
import org.springframework.social.vkontakte.api.VKontakteDate;
import org.springframework.social.vkontakte.api.VKontakteProfile;
import org.springframework.social.vkontakte.connect.VKontakteConnectionFactory;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by mavarazy on 11/24/14.
 */
public class VKontakteSocialAdapter implements SocialConnectionAdapter<VKontakte>{

    final private VKontakteConnectionFactory vKontakteConnectionFactory;

    public VKontakteSocialAdapter(VKontakteConnectionFactory vKontakteConnectionFactory) {
        this.vKontakteConnectionFactory = vKontakteConnectionFactory;
    }

    public ConnectionFactory<VKontakte> getConnectionFactory() {
        return vKontakteConnectionFactory;
    }

    @Override
    public PlayerProfile fetchPlayerProfile(VKontakte api) {
        VKontakteProfile profile = api.usersOperations().getUser();
        // Step 2. Generating appropriate GameProfile to return
        return new PlayerProfile()
            .addSocialConnection(toConnectionKey(profile.getUid()))
            .setFirstName(profile.getFirstName())
            .setNickName(profile.getScreenName())
            .setLastName(profile.getLastName())
            .setBirthDate(toDate(profile.getBirthDate()))
            .setGender(PlayerGender.parse(profile.getGender()));
    }

    public String getEmail(VKontakte api) {
        return null;
    }

    @Override
    public Collection<ConnectionKey> fetchConnections(VKontakte api) {
        Collection<ConnectionKey> connections = api.
            friendsOperations().
            get().
            stream().
            map((profile) -> toConnectionKey(profile.getUid())).
            collect(Collectors.toList());
        return connections;
    }

    @Override
    public Pair<String, String> toImageUrl(Connection<VKontakte> connectionKey) {
        // Step 1. Fetching profile
        VKontakteProfile profile = connectionKey.getApi().usersOperations().getUser();
        // Step 2. Processing images
        return new ImmutablePair<String, String>(profile.getPhotoBig(), profile.getPhotoMedium());
    }

@Override
    public ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        // Step 1. Establishing connection with Facebook
        Connection<VKontakte> connection = vKontakteConnectionFactory.createConnection(accessGrant.toAccessGrant());
        // Step 2. Generating appropriate ConnectionData
        return connection.createData();
    }

    @Override
    public ConnectionData toConnectionData(SocialConnectionData connectionData) {
        return new ConnectionData(connectionData.getProviderId(),
            connectionData.getProviderUserId(),
            "",
            "https://api.vk.com/method/users.get?users=" + connectionData.getProviderUserId(),
            "",
            connectionData.getAccessToken(),
            connectionData.getSecret(),
            connectionData.getRefreshToken(),
            connectionData.getExpireTime());
    }

    public Date toDate(VKontakteDate date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(date.getYear(), date.getMonth(), date.getDay());
        return calendar.getTime();
    }

}
