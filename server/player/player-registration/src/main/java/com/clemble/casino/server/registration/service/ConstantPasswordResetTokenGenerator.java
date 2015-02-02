package com.clemble.casino.server.registration.service;

/**
 * Created by mavarazy on 2/2/15.
 */
public class ConstantPasswordResetTokenGenerator implements PasswordResetTokenGenerator {

    final private String constant;

    public ConstantPasswordResetTokenGenerator(String constant) {
        this.constant = constant;
    }

    @Override
    public String generate() {
        return constant;
    }

}
