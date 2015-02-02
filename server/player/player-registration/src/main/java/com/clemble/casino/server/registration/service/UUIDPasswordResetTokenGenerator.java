package com.clemble.casino.server.registration.service;

import java.util.UUID;

/**
 * Created by mavarazy on 2/2/15.
 */
public class UUIDPasswordResetTokenGenerator implements PasswordResetTokenGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
