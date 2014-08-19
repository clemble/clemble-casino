package com.clemble.casino.server.goal;

import com.clemble.casino.goal.GoalKey;
import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.id.KeyFactory;

/**
 * Created by mavarazy on 8/19/14.
 */
public class GoalKeyGenerator implements KeyGenerator {

    final private KeyFactory keyFactory;

    public GoalKeyGenerator(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    public GoalKey generate(String player) {
        return new GoalKey(player, keyFactory.generate());
    }
}
