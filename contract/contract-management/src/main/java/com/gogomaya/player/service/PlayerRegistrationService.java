package com.gogomaya.player.service;

import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.web.PlayerLoginRequest;
import com.gogomaya.player.web.PlayerRegistrationRequest;
import com.gogomaya.player.web.PlayerSocialRegistrationRequest;

public interface PlayerRegistrationService {

    public PlayerIdentity login(final PlayerLoginRequest playerLoginRequest);

    public PlayerIdentity createPlayer(final PlayerRegistrationRequest registrationRequest);

    public PlayerIdentity createSocialPlayer(final PlayerSocialRegistrationRequest socialConnectionData);

}
