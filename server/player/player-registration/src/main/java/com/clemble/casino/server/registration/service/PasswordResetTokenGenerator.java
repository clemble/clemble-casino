package com.clemble.casino.server.registration.service;

import com.clemble.casino.server.KeyGenerator;

/**
 * Created by mavarazy on 2/2/15.
 */
public interface PasswordResetTokenGenerator extends KeyGenerator {

    public String generate();

}
