package com.clemble.casino.server.security;

import com.clemble.casino.security.ClembleConsumerDetails;
import com.clemble.casino.registration.PlayerToken;

public interface PlayerTokenFactory {

    // TODO make a separate management effort for Tokens & token protocol
    public PlayerToken create(String player, ClembleConsumerDetails consumerDetails);

}
