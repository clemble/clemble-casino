package com.clemble.casino.integration.player;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;

public interface PlayerOperations {

    public Player createPlayer();

    public Player login(PlayerLoginRequest credential);

    public Player createPlayer(PlayerProfile playerProfile);

    public Player createPlayer(PlayerRegistrationRequest registrationRequest);

}
