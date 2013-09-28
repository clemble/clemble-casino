package com.gogomaya.server.integration.player.profile;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.integration.player.Player;

public interface ProfileOperations {

    public PlayerProfile get(Player player, String playerId);

    public PlayerProfile put(Player player, String playerId, PlayerProfile newProfile);

}
