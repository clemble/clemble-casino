package com.clemble.casino.player.service;

import com.clemble.casino.player.security.PlayerToken;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.player.web.PlayerSocialRegistrationRequest;

public interface PlayerRegistrationService {

    public PlayerToken login(final PlayerLoginRequest playerLoginRequest);

    public PlayerToken createPlayer(final PlayerRegistrationRequest registrationRequest);

    public PlayerToken createSocialPlayer(final PlayerSocialRegistrationRequest socialConnectionData);

}
