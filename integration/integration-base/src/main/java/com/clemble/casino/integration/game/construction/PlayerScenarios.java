package com.clemble.casino.integration.game.construction;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.client.ClembleCasinoRegistrationOperations;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.SocialAccessGrant;
import com.clemble.casino.player.SocialConnectionData;
import com.clemble.casino.player.web.PlayerRegistrationRequest;

public interface PlayerScenarios extends ClembleCasinoRegistrationOperations {

    public ClembleCasinoOperations createPlayer();

    public ClembleCasinoOperations createPlayer(PlayerProfile playerProfile);

    public ClembleCasinoOperations createPlayer(SocialAccessGrant socialConnectionData);

    public ClembleCasinoOperations createPlayer(SocialConnectionData socialConnectionData);

    public ClembleCasinoOperations createPlayer(PlayerRegistrationRequest playerRegistrationRequest);

}
