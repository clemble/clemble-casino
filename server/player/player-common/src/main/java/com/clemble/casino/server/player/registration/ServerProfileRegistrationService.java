package com.clemble.casino.server.player.registration;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.server.ServerService;

public interface ServerProfileRegistrationService extends ServerService {

    public PlayerProfile create(final PlayerProfile playerProfile);

    public PlayerProfile create(final SocialConnectionData socialConnectionData);

    public PlayerProfile create(final SocialAccessGrant accessGrant);

}
