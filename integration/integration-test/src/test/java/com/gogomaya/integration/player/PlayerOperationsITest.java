package com.gogomaya.integration.player;

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

import com.gogomaya.player.PlayerCategory;
import com.gogomaya.player.PlayerGender;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.player.security.PlayerCredential;
import com.gogomaya.player.security.PlayerIdentity;
import com.gogomaya.player.web.PlayerLoginRequest;
import com.gogomaya.player.web.PlayerRegistrationRequest;
import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.server.test.RedisCleaner;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { TestConfiguration.class })
@TestExecutionListeners(listeners = { RedisCleaner.class, DependencyInjectionTestExecutionListener.class })
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
        PlayerProfile profile = new PlayerProfile().setCategory(PlayerCategory.Amateur).setFirstName("Anton").setLastName("Oparin").setGender(PlayerGender.M)
                .setNickName("mavarazy");

        Player player = playerOperations.createPlayer(profile);

        assertNotNull(player);
        assertEquals(player.getProfile().getCategory(), PlayerCategory.Amateur);
        assertEquals(player.getProfile().getFirstName(), "Anton");
        assertEquals(player.getProfile().getLastName(), "Oparin");
        assertEquals(player.getProfile().getGender(), PlayerGender.M);
        assertEquals(player.getProfile().getNickName(), "mavarazy");
    }

    @Test
    public void createPlayerUsingRegistrationRequest() {
        PlayerProfile profile = new PlayerProfile().setCategory(PlayerCategory.Amateur).setFirstName("Anton").setLastName("Oparin").setGender(PlayerGender.M)
                .setNickName("mavarazy");

        PlayerCredential playerCredential = new PlayerCredential().setEmail("mavarazy@gmail.com").setPassword("23443545");
        PlayerIdentity playerIdentity = new PlayerIdentity()
            .setSecret(UUID.randomUUID().toString())
            .setDevice(UUID.randomUUID().toString());

        PlayerRegistrationRequest registrationRequest = new PlayerRegistrationRequest(profile, playerCredential, playerIdentity);

        Player player = playerOperations.createPlayer(registrationRequest);

        assertNotNull(player);
        assertEquals(player.getProfile().getCategory(), PlayerCategory.Amateur);
        assertEquals(player.getProfile().getFirstName(), "Anton");
        assertEquals(player.getProfile().getLastName(), "Oparin");
        assertEquals(player.getProfile().getGender(), PlayerGender.M);
        assertEquals(player.getProfile().getNickName(), "mavarazy");

        try {
            System.out.println(new ObjectMapper().writeValueAsString(registrationRequest));
        } catch (Throwable error) {
            throw new RuntimeException(error);
        }
    }

    @Test
    public void loginExistingUser() {
        PlayerProfile profile = new PlayerProfile().setCategory(PlayerCategory.Amateur).setFirstName("Anton").setLastName("Oparin").setGender(PlayerGender.M)
                .setNickName("mavarazy");

        Player player = playerOperations.createPlayer(profile);

        PlayerCredential loginCredential = new PlayerCredential()
            .setEmail(player.getCredential().getEmail())
            .setPassword(player.getCredential().getPassword());
        PlayerIdentity playerIdentity = new PlayerIdentity()
            .setSecret(UUID.randomUUID().toString())
            .setDevice(UUID.randomUUID().toString());

        Player loginPlayer = playerOperations.login(new PlayerLoginRequest(playerIdentity, loginCredential));

        assertEquals(loginPlayer.getPlayerId(), player.getPlayerId());

        assertEquals(loginPlayer.getCredential().getEmail(), player.getCredential().getEmail());
        assertEquals(loginPlayer.getCredential().getPassword(), player.getCredential().getPassword());

        assertEquals(loginPlayer.getIdentity().getSecret(), playerIdentity.getSecret());
        assertEquals(loginPlayer.getIdentity().getPlayerId(), player.getIdentity().getPlayerId());

    }
}
