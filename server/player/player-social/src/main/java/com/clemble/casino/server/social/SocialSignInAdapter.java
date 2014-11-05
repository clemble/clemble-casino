package com.clemble.casino.server.social;

import com.clemble.casino.player.PlayerPresence;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.security.PlayerTokenUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by mavarazy on 11/2/14.
 */
public class SocialSignInAdapter implements SignInAdapter {

    final private String host;
    final private PlayerTokenUtils tokenUtils;
    final private SocialConnectionDataAdapter connectionDataAdapter;

    public SocialSignInAdapter(String host, PlayerTokenUtils tokenUtils, SocialConnectionDataAdapter connectionDataAdapter) {
        this.host = host;
        this.tokenUtils = tokenUtils;
        this.connectionDataAdapter = connectionDataAdapter;
    }

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
        // Step 1. Fetching connection data
        ConnectionData connectionData = connection.createData();
        // Step 2. Creating internal social connection
        String player =  connectionDataAdapter.register(connectionData);
        // Step 3. Adding player Cookie to response
        tokenUtils.updateResponse(player, (HttpServletResponse) request.getNativeResponse());
        // Step 4. Redirecting to url
        return host;
    }
}
