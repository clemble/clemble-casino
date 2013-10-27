package com.clemble.casino.server.player;

import java.util.UUID;

public class UUIDPlayerIdGenerator implements PlayerIdGenerator {

    @Override
    public String newId() {
        return "aaa" + UUID.randomUUID().toString();
    }

}
