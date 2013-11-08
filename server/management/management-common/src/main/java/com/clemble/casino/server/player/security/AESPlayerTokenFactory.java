package com.clemble.casino.server.player.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.player.security.PlayerToken;

public class AESPlayerTokenFactory implements PlayerTokenFactory {

    final private KeyGenerator AES_KEY_GENERATOR;

    public AESPlayerTokenFactory() throws NoSuchAlgorithmException {
        AES_KEY_GENERATOR = KeyGenerator.getInstance("AES");
        AES_KEY_GENERATOR.init(256, new SecureRandom());
    }

    @Override
    public PlayerToken create(String player, ClembleConsumerDetails consumerDetails) {
        return new PlayerToken(player, AES_KEY_GENERATOR.generateKey(), consumerDetails.getConsumerKey(), "");
    }

}
