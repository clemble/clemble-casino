package com.gogomaya.server.integration.player.profile;

import static com.google.common.base.Preconditions.checkNotNull;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.player.PlayerProfile;

public class PlayerProfileOperations {

    final private Player player;
    final private ProfileOperations profileOperations;

    public PlayerProfileOperations(Player player, ProfileOperations profileOperations) {
        this.player = checkNotNull(player);
        this.profileOperations = checkNotNull(profileOperations);
    }

    public PlayerProfile get() {
        return get(player.getPlayerId());
    }

    public PlayerProfile get(long playerId) {
        return profileOperations.get(player, playerId);
    }

    public PlayerProfile set(PlayerProfile newProfile) {
        return set(newProfile, player.getPlayerId());
    }

    public PlayerProfile set(PlayerProfile newProfile, long playerId) {
        return profileOperations.put(player, playerId, newProfile);
    }

}
