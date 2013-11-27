package com.clemble.casino.client;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.security.PlayerCredential;

public interface ClembleCasinoRegistrationOperations {

    public ClembleCasinoOperations login(final PlayerCredential playerCredentials);

    public ClembleCasinoOperations createPlayer(final PlayerCredential playerCredential, final PlayerProfile playerProfile);

    public ClembleCasinoOperations createSocialPlayer(final PlayerCredential playerCredential, final SocialConnectionData socialConnectionData);

}
