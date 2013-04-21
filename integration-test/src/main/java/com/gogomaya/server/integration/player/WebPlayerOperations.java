package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.web.registration.RegistrationLoginController;
import com.gogomaya.server.web.registration.RegistrationSignInContoller;

public class WebPlayerOperations extends AbstractPlayerOperations {

    final private RegistrationSignInContoller signInContoller;

    final private RegistrationLoginController loginController;

    @Inject
    public WebPlayerOperations(RegistrationSignInContoller signInContoller, RegistrationLoginController loginController) {
        this.signInContoller = checkNotNull(signInContoller);
        this.loginController = checkNotNull(loginController);
    }

    @Override
    public Player createPlayer(RegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = signInContoller.createUser(registrationRequest);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        return new Player().setPlayerId(playerIdentity.getPlayerId()).setIdentity(playerIdentity).setProfile(registrationRequest.getPlayerProfile())
                .setCredential(registrationRequest.getPlayerCredential());

    }

    @Override
    public Player login(PlayerCredential credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerIdentity playerIdentity = loginController.createUser(credential);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        return new Player().setPlayerId(playerIdentity.getPlayerId()).setCredential(credential).setIdentity(playerIdentity);
    }

}
