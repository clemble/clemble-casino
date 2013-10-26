package com.clemble.casino.server.player;

import java.util.UUID;

public class UUIDPlayerIdentityGenerator implements PlayerIdentifierGenerator {

    @Override
    public String newIdentifier() {
        return UUID.randomUUID().toString();
    }

}
