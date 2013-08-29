package com.gogomaya.server.integration.player;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.web.RegistrationRequest;

public interface PlayerOperations {

    public Player login(PlayerCredential credential);

    public Player createPlayer();

    public Player createPlayer(PlayerProfile playerProfile);

    public Player createPlayer(RegistrationRequest registrationRequest);

}
