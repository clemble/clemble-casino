package com.clemble.casino.server.social.adapter;

import static com.clemble.casino.utils.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Calendar;
import java.util.Date;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.social.SocialConnectionAdapter;

public class FacebookSocialAdapter extends SocialConnectionAdapter<Facebook> {

    final private FacebookConnectionFactory facebookConnectionFactory;

    public FacebookSocialAdapter(FacebookConnectionFactory facebookConnectionFactory) {
        super("facebook");
        this.facebookConnectionFactory = checkNotNull(facebookConnectionFactory);
    }

    private Date readDate(String facebookBirthDate) {
        if (!isNullOrEmpty(facebookBirthDate)) {
            String[] date = facebookBirthDate.split("/");
            if(date.length != 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                calendar.set(Integer.valueOf(date[2]), 1 + Integer.valueOf(date[0]), Integer.valueOf(date[1]));
                return calendar.getTime();
            }
        }
        return null;
    }

    @Override
    public PlayerProfile fetchGamerProfile(Facebook facebook) {
        // Step 1. Retrieving facebook profile for associated user
        FacebookProfile facebookProfile = facebook.userOperations().getUserProfile();
        // Step 2. Generating appropriate GameProfile to return
        return new PlayerProfile()
            .addSocialConnection(new ConnectionKey("facebook", facebookProfile.getId()))
            .setFirstName(facebookProfile.getFirstName())
            .setNickName(facebookProfile.getName())
            .setLastName(facebookProfile.getLastName())
            .setBirthDate(readDate(facebookProfile.getBirthday()))
            .setGender(PlayerGender.parse(facebookProfile.getGender()))
            .setImageUrl("http://graph.facebook.com/" + facebookProfile.getId() + "/picture")
            .setNickName(facebookProfile.getUsername());
    }

    @Override
    public ConnectionData toConnectionData(SocialAccessGrant accessGrant) {
        // Step 1. Establishing connection with Facebook
        Connection<Facebook> connection = facebookConnectionFactory.createConnection(accessGrant.toAccessGrant());
        // Step 2. Generating appropriate ConnectionData
        return connection.createData();
    }

    @Override
    public ConnectionData toConnectionData(SocialConnectionData connectionData) {
        return new ConnectionData(connectionData.getProviderId(),
                   connectionData.getProviderUserId(),
                   "",
                   "http://facebook.com/profile.php?id=" + connectionData.getProviderUserId(),
                   "http://graph.facebook.com/" + connectionData.getProviderUserId() + "/picture",
                   connectionData.getAccessToken(),
                   connectionData.getSecret(),
                   connectionData.getRefreshToken(),
                   connectionData.getExpireTime());
    }

}
