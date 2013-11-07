package com.clemble.casino.server.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;

public class ObjectGeneratorInitializer {

    public static void initialize() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        final KeyPair keyPair = generator.generateKeyPair();

        ObjectGenerator.register(PublicKey.class, new AbstractValueGenerator<PublicKey>() {

            @Override
            public PublicKey generate() {
                return keyPair.getPublic();
            }

        });

        ObjectGenerator.register(PrivateKey.class, new AbstractValueGenerator<PrivateKey>() {

            @Override
            public PrivateKey generate() {
                return keyPair.getPrivate();
            }
        });
    }

}
