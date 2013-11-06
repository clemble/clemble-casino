package com.clemble.casino.server.repository.player;

import com.clemble.casino.player.PlayerProfile;

public interface PlayerProfileRepository {

    public PlayerProfile findOne(String player);

    public PlayerProfile save(PlayerProfile playerProfile);

    public void deleteAll();

    public long count();

}
