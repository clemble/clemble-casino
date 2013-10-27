package com.clemble.casino.game.id;

import java.util.UUID;

public class UUIDGameIdGenerator implements GameIdGenerator {

    /* 
     * TODO must generate identifier based on the current network configuration, and consider
     * table structure information used
     */
    @Override
    public String newId() {
        return "aaa" + UUID.randomUUID().toString();
    }

}
