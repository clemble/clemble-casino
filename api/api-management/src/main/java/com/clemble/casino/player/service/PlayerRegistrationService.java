package com.clemble.casino.player.service;

import com.clemble.casino.player.security.PlayerIdentity;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;

public interface PlayerRegistrationService {

    public PlayerIdentity login(final PlayerLoginRequest playerLoginRequest);

    public PlayerIdentity createPlayer(final PlayerRegistrationRequest registrationRequest);

    public PlayerIdentity createSocialPlayer(final PlayerSocialRegistrationRequest socialConnectionData);

}
