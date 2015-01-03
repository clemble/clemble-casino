package com.clemble.casino.server.email;

import com.clemble.casino.server.email.repository.PlayerEmailRepository;
import com.clemble.casino.server.email.service.ServerPlayerEmailService;
import com.clemble.casino.server.email.spring.PlayerEmailSpringConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 12/6/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PlayerEmailSpringConfiguration.class)
public class TestSimpleEmailVerification {

    @Autowired
    public ServerPlayerEmailService playerEmailService;

    @Autowired
    public PlayerEmailRepository emailRepository;

    @Test
    public void test() {
        // Step 0. Generating player & email
        String player = RandomStringUtils.randomAlphabetic(10);
        String email = RandomStringUtils.randomAlphabetic(10) + "@gmail.com";
        // Step 0.1 Checking no previous records exist
        Assert.assertNull(emailRepository.findOne(player));
        // Step 1. Generating URL
        String url = playerEmailService.add(player, email, false);
        Assert.assertNotNull(url);
        String verificationCode = url.substring(url.lastIndexOf("=") + 1);
        Assert.assertTrue(playerEmailService.verify(verificationCode));
        // Step 2. Checking values match
        Assert.assertEquals(emailRepository.findOne(player).getEmail(), email);
    }

}
