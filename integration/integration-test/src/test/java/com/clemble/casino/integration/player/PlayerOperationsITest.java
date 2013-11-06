package com.clemble.casino.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.integration.spring.TestConfiguration;
import com.clemble.casino.player.NativePlayerProfile;
import com.clemble.casino.player.PlayerCategory;
import com.clemble.casino.player.PlayerGender;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.player.security.PlayerCredential;
import com.clemble.casino.player.security.PlayerIdentity;
import com.clemble.casino.player.web.PlayerLoginRequest;
import com.clemble.casino.player.web.PlayerRegistrationRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class })
public class PlayerOperationsITest {

    @Autowired
    public PlayerOperations playerOperations;

    @Test
    public void createRandomPlayer() {
        Player player = playerOperations.createPlayer();

        assertNotNull(player);
    }

    @Test
    public void createPlayerUsingProfileTest() {
        PlayerProfile profile = new NativePlayerProfile().setCategory(PlayerCategory.Amateur).setFirstName("Anton").setLastName("Oparin").setGender(PlayerGender.M)
                .setNickName("mavarazy");

        Player player = playerOperations.createPlayer(profile);

        assertNotNull(player);
        assertEquals(player.<NativePlayerProfile>getProfile().getCategory(), PlayerCategory.Amateur);
        assertEquals(player.<NativePlayerProfile>getProfile().getFirstName(), "Anton");
        assertEquals(player.<NativePlayerProfile>getProfile().getLastName(), "Oparin");
        assertEquals(player.<NativePlayerProfile>getProfile().getGender(), PlayerGender.M);
        assertEquals(player.<NativePlayerProfile>getProfile().getNickName(), "mavarazy");
    }

    @Test
    public void createPlayerUsingRegistrationRequest() {
        PlayerProfile profile = new NativePlayerProfile().setCategory(PlayerCategory.Amateur).setFirstName("Anton").setLastName("Oparin").setGender(PlayerGender.M)
                .setNickName("mavarazy");

        PlayerCredential playerCredential = new PlayerCredential().setEmail("mavarazy@gmail.com").setPassword("23443545");
        PlayerIdentity playerIdentity = new PlayerIdentity()
            .setSecret(UUID.randomUUID().toString())
            .setDevice(UUID.randomUUID().toString());

        PlayerRegistrationRequest registrationRequest = new PlayerRegistrationRequest(profile, playerCredential, playerIdentity);

        Player player = playerOperations.createPlayer(registrationRequest);

        assertNotNull(player);
        assertEquals(player.<NativePlayerProfile>getProfile().getCategory(), PlayerCategory.Amateur);
        assertEquals(player.<NativePlayerProfile>getProfile().getFirstName(), "Anton");
        assertEquals(player.<NativePlayerProfile>getProfile().getLastName(), "Oparin");
        assertEquals(player.<NativePlayerProfile>getProfile().getGender(), PlayerGender.M);
        assertEquals(player.<NativePlayerProfile>getProfile().getNickName(), "mavarazy");

        try {
            System.out.println(new ObjectMapper().writeValueAsString(registrationRequest));
        } catch (Throwable error) {
            throw new RuntimeException(error);
        }
    }

    @Test
    public void loginExistingUser() {
        PlayerProfile profile = new NativePlayerProfile().setCategory(PlayerCategory.Amateur).setFirstName("Anton").setLastName("Oparin").setGender(PlayerGender.M)
                .setNickName("mavarazy");

        Player player = playerOperations.createPlayer(profile);

        PlayerCredential loginCredential = new PlayerCredential()
            .setEmail(player.getCredential().getEmail())
            .setPassword(player.getCredential().getPassword());
        PlayerIdentity playerIdentity = new PlayerIdentity()
            .setSecret(UUID.randomUUID().toString())
            .setDevice(UUID.randomUUID().toString());

        Player loginPlayer = playerOperations.login(new PlayerLoginRequest(playerIdentity, loginCredential));

        assertEquals(loginPlayer.getPlayer(), player.getPlayer());

        assertEquals(loginPlayer.getCredential().getEmail(), player.getCredential().getEmail());
        assertEquals(loginPlayer.getCredential().getPassword(), player.getCredential().getPassword());

        assertEquals(loginPlayer.getIdentity().getSecret(), playerIdentity.getSecret());
        assertEquals(loginPlayer.getIdentity().getPlayer(), player.getIdentity().getPlayer());

    }
}
