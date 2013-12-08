package com.clemble.casino.server.player.registration;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;

public interface PlayerProfileRegistrationServerService {

    public PlayerProfile createPlayerProfile(final PlayerProfile playerProfile);

    public PlayerProfile createPlayerProfile(final SocialConnectionData socialConnectionData);

    public PlayerProfile createPlayerProfile(final SocialAccessGrant accessGrant);

}
