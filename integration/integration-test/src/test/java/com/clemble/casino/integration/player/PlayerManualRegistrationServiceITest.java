package com.clemble.casino.integration.player;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.client.ClembleCasinoOperations;
import com.clemble.casino.integration.game.construction.PlayerScenarios;
import com.clemble.casino.integration.spring.IntegrationTestSpringConfiguration;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.test.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { IntegrationTestSpringConfiguration.class })
public class PlayerManualRegistrationServiceITest {

    @Autowired
    public PlayerScenarios playerScenarios;

    @Test
    public void createAndLogin(){
        // Step 1. Generating player credential & profile
        PlayerCredential credential = new PlayerCredential()
            .setEmail(RandomStringUtils.randomAlphabetic(10) + "@gmail.com")
            .setPassword(RandomStringUtils.random(10));
        PlayerProfile playerProfile = ObjectGenerator.generate(PlayerProfile.class)
                .setBirthDate(new Date(0))
                .setSocialConnections(null);
        // Step 2. Creating CasinoOperations with this credentials and Profile
        ClembleCasinoOperations origA = playerScenarios.createPlayer(credential, playerProfile);
        // Step 3. Creating CasinoOperations by just login
        ClembleCasinoOperations origB = playerScenarios.login(credential);
        // Step 4. Checking they are the same
        assertEquals(origA.getPlayer(), origB.getPlayer());
        assertEquals(origA.profileOperations().myProfile(), origB.profileOperations().myProfile());
        assertEquals(origA.presenceOperations().myPresence(), origB.presenceOperations().myPresence());
        assertEquals(origA.accountService().myAccount(), origB.accountService().myAccount());
    }

    @Test
    public void createWithoutNick(){
        String nick = RandomStringUtils.randomAlphabetic(10);
        // Step 1. Generating player credential & profile
        PlayerCredential credential = new PlayerCredential()
            .setEmail(nick + "@gmail.com")
            .setPassword(RandomStringUtils.random(10));
        PlayerProfile playerProfile = ObjectGenerator.generate(PlayerProfile.class)
                .setNickName(null)
                .setBirthDate(new Date(0))
                .setSocialConnections(null);
        // Step 2. Creating CasinoOperations with this credentials and Profile
        ClembleCasinoOperations origA = playerScenarios.createPlayer(credential, playerProfile);
        // Step 3. Checking nick matches
        assertEquals(nick, origA.profileOperations().myProfile().getNickName());
    }
}
