package com.clemble.casino.server.player.security;

import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerToken;

public interface PlayerTokenFactory {

    public PlayerToken create(String player, ClembleConsumerDetails consumerDetails);

}
