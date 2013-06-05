package com.gogomaya.server.integration.player;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.springframework.transaction.annotation.Transactional;

import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.security.PlayerSession;
import com.gogomaya.server.player.wallet.PlayerWallet;
import com.gogomaya.server.player.web.RegistrationRequest;

public interface PlayerOperations {

    @Given("$A player")
    @When("Player created")
    @Transactional
    public Player createPlayer();

    @Given("$A player with $profile")
    @When("$A player created with $profile")
    public Player createPlayer(PlayerProfile playerProfile);

    @Given("$A player registers with $Request")
    @When("$A player registers with $profile")
    public Player createPlayer(RegistrationRequest registrationRequest);

    @Given("$A logins")
    @When("$A logins")
    public Player login(PlayerCredential credential);

    public PlayerSession startSession(Player player);

    public PlayerWallet wallet(Player playerId, long playerWalletId);

}
