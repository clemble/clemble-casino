package com.clemble.casino.integration.player;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;

public interface PlayerOperations {

    public ClembleCasinoOperations createPlayer();

    public ClembleCasinoOperations login(PlayerLoginRequest credential);

    public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile);

    public ClembleCasinoOperations createPlayer(PlayerRegistrationRequest registrationRequest);

}
