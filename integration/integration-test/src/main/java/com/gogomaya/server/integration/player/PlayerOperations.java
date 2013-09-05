package com.gogomaya.server.integration.player;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.web.PlayerLoginRequest;
import com.gogomaya.player.web.PlayerRegistrationRequest;

public interface PlayerOperations {

    public Player createPlayer();

    public Player login(PlayerLoginRequest credential);

    public Player createPlayer(PlayerProfile playerProfile);

    public Player createPlayer(PlayerRegistrationRequest registrationRequest);

}
