package com.clemble.casino.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.security.oauth.common.signature.RSAKeySecret;

import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.client.ClientDetails;

public class ClembleConsumerDetailUtils {

    public static RSAKeySecret randomKey() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair keyPair = generator.generateKeyPair();
            return new RSAKeySecret(keyPair.getPrivate(), keyPair.getPublic());
        } catch (NoSuchAlgorithmException algorithmException) {
            throw new IllegalArgumentException(algorithmException);
        }
    }

    public static ClembleConsumerDetails generateDetails(){
        return new ClembleConsumerDetails(UUID.randomUUID().toString(), "Android", randomKey(), null, new ClientDetails("Android"));
    }

}
