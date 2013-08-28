package com.gogomaya.server.integration.player.profile;

import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.integration.player.Player;

public interface ProfileOperations {

    public PlayerProfile get(Player player, long playerId);

    public PlayerProfile put(Player player, long playerId, PlayerProfile newProfile);

}
