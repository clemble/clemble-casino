package com.gogomaya.server.integration.player;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.web.player.registration.RegistrationLoginController;
import com.gogomaya.server.web.player.registration.RegistrationSignInContoller;
import com.gogomaya.server.web.player.wallet.WalletController;

public class WebPlayerOperations extends AbstractPlayerOperations {

    final private RegistrationSignInContoller signInContoller;

    final private RegistrationLoginController loginController;

    final private WalletController walletController;

    @Inject
    public WebPlayerOperations(RegistrationSignInContoller signInContoller, RegistrationLoginController loginController, WalletController walletController) {
        this.signInContoller = checkNotNull(signInContoller);
        this.loginController = checkNotNull(loginController);
        this.walletController = checkNotNull(walletController);
    }

    @Override
    public Player createPlayer(RegistrationRequest registrationRequest) {
        // Step 0. Sanity check
        checkNotNull(registrationRequest);
        // Step 1. Performing actual player creation
        PlayerIdentity playerIdentity = signInContoller.createUser(registrationRequest);
        checkNotNull(playerIdentity);
        // Step 2. Generating Player from created request
        return new Player(this).setPlayerId(playerIdentity.getPlayerId()).setIdentity(playerIdentity).setProfile(registrationRequest.getPlayerProfile())
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
        return new Player(this).setPlayerId(playerIdentity.getPlayerId()).setCredential(credential).setIdentity(playerIdentity);
    }

    @Override
    public PlayerWallet wallet(Player player, long playerWalletId) {
        return walletController.get(player.getPlayerId(), playerWalletId);
    }

}
