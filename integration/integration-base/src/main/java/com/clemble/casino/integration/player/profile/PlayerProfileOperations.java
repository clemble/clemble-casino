package com.clemble.casino.integration.player.profile;

import static com.clemble.casino.utils.Preconditions.checkNotNull;

import com.clemble.casino.integration.player.Player;
import com.clemble.casino.player.PlayerProfile;

public class PlayerProfileOperations {

    final private Player player;
    final private ProfileOperations profileOperations;

    public PlayerProfileOperations(Player player, ProfileOperations profileOperations) {
        this.player = checkNotNull(player);
        this.profileOperations = checkNotNull(profileOperations);
    }

    public PlayerProfile get() {
        return get(player.getPlayer());
    }

    public PlayerProfile get(String playerId) {
        return profileOperations.get(player, playerId);
    }

    public PlayerProfile set(PlayerProfile newProfile) {
        return set(newProfile, player.getPlayer());
    }

    public PlayerProfile set(PlayerProfile newProfile, String playerId) {
        return profileOperations.put(player, playerId, newProfile);
    }

}
