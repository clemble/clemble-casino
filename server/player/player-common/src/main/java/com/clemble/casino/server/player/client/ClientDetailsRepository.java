package com.clemble.casino.server.player.client;

import java.util.List;

import com.clemble.casino.player.client.ClientDetails;

public interface ClientDetailsRepository {

    public ClientDetails findOne(String client);

    public List<ClientDetails> findByPlayer(String player);

}
