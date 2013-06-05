package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.web.player.registration.RegistrationLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;
import com.gogomaya.server.web.player.session.PlayerSessionController;
import com.gogomaya.server.web.player.wallet.WalletController;

public class WebPlayerOperations extends AbstractPlayerOperations {

    final private RegistrationSignInContoller signInContoller;

    final private RegistrationLoginController loginController;

    final private PlayerSessionController sessionController;

    final private WalletController walletController;

    @Inject
    public WebPlayerOperations(RegistrationSignInContoller signInContoller,
            RegistrationLoginController loginController,
            WalletController walletController,
            PlayerSessionController sessionController) {
        this.signInContoller = checkNotNull(signInContoller);
        this.loginController = checkNotNull(loginController);
        this.walletController = checkNotNull(walletController);
        this.sessionController = checkNotNull(sessionController);
    }

    @Override
    public Player createPlayer(RegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = signInContoller.createUser(registrationRequest);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        Player player = new Player(this).setPlayerId(playerIdentity.getPlayerId()).setIdentity(playerIdentity)
                .setProfile(registrationRequest.getPlayerProfile()).setCredential(registrationRequest.getPlayerCredential());
        // Step 3. Creating player session
        player.setSession(startSession(player));
        // Step 4. Returning player session result
        return player;

    }

    @Override
    public Player login(PlayerCredential credential) {
        // Step 0. Sanity check
        checkNotNull(credential);
        // Step 1. Performing actual player login
        PlayerIdentity playerIdentity = loginController.createUser(credential);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from credentials
        Player player = new Player(this).setPlayerId(playerIdentity.getPlayerId()).setCredential(credential).setIdentity(playerIdentity);
        // Step 3. Creating player session
        player.setSession(startSession(player));
        // Step 4. Returning player session result
        return player;
    }

    @Override
    public PlayerWallet wallet(Player player, long playerWalletId) {
        return walletController.get(player.getPlayerId(), playerWalletId);
    }

    @Override
    public PlayerSession startSession(Player player) {
        return sessionController.startSession(player.getPlayerId());
    }

}
