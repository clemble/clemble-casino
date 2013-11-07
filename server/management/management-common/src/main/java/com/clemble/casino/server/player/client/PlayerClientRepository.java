package com.clemble.casino.server.player.client;

import java.util.List;

import com.clemble.casino.player.client.PlayerClient;

public interface PlayerClientRepository {

    public PlayerClient find(String client);

    public List<PlayerClient> findByPlayer(String player);

}
