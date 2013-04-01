package com.gogomaya.server.integration.client;

import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerIdentity;

public interface GogomayaClient {

    public PlayerIdentity createProfile(PlayerProfile playerProfile);

}
