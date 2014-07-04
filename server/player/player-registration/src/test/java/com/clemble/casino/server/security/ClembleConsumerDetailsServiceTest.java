package com.clemble.casino.server.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth.common.signature.RSAKeySecret;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.player.client.ClembleConsumerDetails;
import com.clemble.casino.server.spring.web.OAuthSpringConfiguration;
import com.clemble.test.random.AbstractValueGenerator;
import com.clemble.test.random.ObjectGenerator;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OAuthSpringConfiguration.class)
public class ClembleConsumerDetailsServiceTest {

    @Autowired
    public ClembleConsumerDetailsService consumerDetailsService;

    @BeforeClass
    public static void setUp() throws NoSuchAlgorithmException {
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

    @Test
    public void testSaving() {
        ClembleConsumerDetails consumerDetails = ObjectGenerator.generate(ClembleConsumerDetails.class);
        consumerDetailsService.save(consumerDetails);
        ClembleConsumerDetails readConsumerDetails = consumerDetailsService.loadConsumerByConsumerKey(consumerDetails.getConsumerKey());
        assertNull(readConsumerDetails.getSignatureSecret().getPrivateKey());
        consumerDetails = new ClembleConsumerDetails(consumerDetails.getConsumerKey(), 
                consumerDetails.getConsumerName(), 
                new RSAKeySecret(consumerDetails.getSignatureSecret().getPublicKey()),
                consumerDetails.getAuthorities(),
                consumerDetails.getClientDetail());
        assertEquals(consumerDetails, readConsumerDetails);
    }
}
