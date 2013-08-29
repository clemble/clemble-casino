package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.web.RegistrationRequest;
import com.gogomaya.server.integration.player.account.AccountOperations;
import com.gogomaya.server.integration.player.listener.PlayerListenerOperations;
import com.gogomaya.server.integration.player.profile.ProfileOperations;
import com.gogomaya.server.integration.player.session.SessionOperations;
import com.gogomaya.server.web.player.registration.PlayerRegistrationController;

public class WebPlayerOperations extends AbstractPlayerOperations {

    final private PlayerRegistrationController registrationController;

    public WebPlayerOperations(PlayerRegistrationController registrationController,
            SessionOperations playerSessionOperations,
            AccountOperations accountOperations,
            PlayerListenerOperations listenerOperations,
            ProfileOperations playerProfileOperations) {
        super(listenerOperations, playerProfileOperations, playerSessionOperations, accountOperations);
        this.registrationController = checkNotNull(registrationController);
    }

    @Override
    public Player createPlayer(RegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = registrationController.createPlayer(registrationRequest);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        Player player = super.create(playerIdentity, registrationRequest.getPlayerCredential());
        // Step 3. Returning player session result
        return player;

    }

    @Override
    public Player login(PlayerCredential credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerIdentity playerIdentity = registrationController.login(credential);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        Player player = super.create(playerIdentity, credential);
        // Step 3. Returning player session result
        return player;
    }

}
