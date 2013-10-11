package com.clemble.casino.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import com.clemble.casino.integration.player.account.AccountOperations;
import com.clemble.casino.integration.player.listener.PlayerListenerOperations;
import com.clemble.casino.integration.player.profile.ProfileOperations;
import com.clemble.casino.integration.player.session.SessionOperations;
import com.clemble.casino.player.security.PlayerIdentity;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;
import com.clemble.casino.server.web.management.PlayerRegistrationController;

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
    public Player createPlayer(PlayerRegistrationRequest registrationRequest) {
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
    public Player login(PlayerLoginRequest credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerIdentity playerIdentity = registrationController.login(credential);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        Player player = super.create(playerIdentity, credential.getPlayerCredential());
        // Step 3. Returning player session result
        return player;
    }

}