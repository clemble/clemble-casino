package com.clemble.casino.server.registration;

import com.clemble.casino.server.KeyGenerator;
import com.clemble.casino.server.id.KeyFactory;

/**
 * Created by mavarazy on 8/19/14.
 */
public class PlayerKeyGenerator implements KeyGenerator {

    final private KeyFactory keyFactory;

    public PlayerKeyGenerator(KeyFactory keyFactory) {
        this.keyFactory = keyFactory;
    }

    public String generate() {
        return keyFactory.generate();
    }

}
