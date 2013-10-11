package com.clemble.casino.integration.player.profile;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.PlayerProfile;

public interface ProfileOperations {

    public PlayerProfile get(Player player, String playerId);

    public PlayerProfile put(Player player, String playerId, PlayerProfile newProfile);

}
