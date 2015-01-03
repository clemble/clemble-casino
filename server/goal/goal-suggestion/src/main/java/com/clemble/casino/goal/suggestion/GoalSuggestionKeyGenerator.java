package com.clemble.casino.goal.suggestion;

import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.key.KeyFactory;

/**
 * Created by mavarazy on 1/3/15.
 */
public class GoalSuggestionKeyGenerator implements KeyGenerator {

    final private KeyFactory keyFactory;

    public GoalSuggestionKeyGenerator(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    public String generate(String player) {
        return player + keyFactory.generate();
    }

}
