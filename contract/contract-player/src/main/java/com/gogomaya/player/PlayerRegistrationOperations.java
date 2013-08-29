package com.gogomaya.player;

import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.web.RegistrationRequest;

public interface PlayerRegistrationOperations {

    public PlayerIdentity login(final PlayerCredential playerCredentials);

    public PlayerIdentity createPlayer(final RegistrationRequest registrationRequest);

    public PlayerIdentity createPlayer(final SocialConnectionData socialConnectionData);

}
