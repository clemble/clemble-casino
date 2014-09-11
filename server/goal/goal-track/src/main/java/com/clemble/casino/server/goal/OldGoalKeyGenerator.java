package com.clemble.casino.server.goal;

import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.key.KeyFactory;

/**
 * Created by mavarazy on 8/19/14.
 */
// TODO depricate this use
public class OldGoalKeyGenerator implements KeyGenerator {

    final private KeyFactory keyFactory;

    public OldGoalKeyGenerator(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    public String generate(String player) {
        return player + keyFactory.generate();
    }
}
