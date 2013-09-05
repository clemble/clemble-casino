package com.gogomaya.server.player.registration;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.SocialConnectionData;

public interface PlayerProfileRegistrationServerService {

    public PlayerProfile createPlayerProfile(final PlayerProfile playerProfile);

    public PlayerProfile createPlayerProfile(final SocialConnectionData socialConnectionData);

}
