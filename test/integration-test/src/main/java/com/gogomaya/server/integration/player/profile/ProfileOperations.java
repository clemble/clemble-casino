package com.gogomaya.server.integration.player.profile;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.PlayerProfile;

public interface ProfileOperations {

    public PlayerProfile get(Player player, long playerId);

    public PlayerProfile put(Player player, long playerId, PlayerProfile newProfile);

}
