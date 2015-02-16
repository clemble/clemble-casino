package com.clemble.casino.server.registration.security;

import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.registration.PlayerCredential;
import com.clemble.casino.registration.PlayerLoginRequest;
import com.clemble.casino.registration.PlayerRegistrationRequest;
import com.clemble.casino.server.registration.controller.PlayerRegistrationController;
import com.clemble.casino.server.registration.repository.ServerPlayerCredentialRepository;
import com.clemble.casino.server.registration.spring.RegistrationSpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 11/1/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RegistrationSpringConfiguration.class)
public class PlayerCredentialsTest {

    @Autowired
    public ServerPlayerCredentialRepository credentialRepository;

    @Autowired
    public PlayerRegistrationController registrationController;

    @Test
    public void testPasswordHashed() {
        // Step 1. Creating random data
        PlayerCredential credential = new PlayerCredential("me@me.me", "1234567");
        PlayerProfile profile = new PlayerProfile();
        PlayerRegistrationRequest registrationRequest = PlayerRegistrationRequest.create(credential, profile);
        // Step 2. Processing RegistrationController
        String createdPlayer = registrationController.register(registrationRequest).getPlayer();
        // Step 3. Checking password was not saved as plain text
        Assert.assertNotEquals(credentialRepository.findOne(createdPlayer), credential.getPassword());
        // Step 4. Checking can login with Credentials
        PlayerLoginRequest logInPlayer = registrationController.login(new PlayerLoginRequest(null, "me@me.me", "1234567"));
        // Step 5. Checking we can login with old credentials and player will be same
        Assert.assertEquals(logInPlayer.getPlayer(), createdPlayer);
    }
}
