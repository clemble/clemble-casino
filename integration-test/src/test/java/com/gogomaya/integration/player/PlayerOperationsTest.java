package com.gogomaya.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jbehave.core.annotations.UsingSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.integration.player.Player;
import com.gogomaya.server.integration.player.PlayerOperations;
import com.gogomaya.server.player.PlayerCategory;
import com.gogomaya.server.player.PlayerGender;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.player.security.PlayerCredential;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.spring.integration.TestConfiguration;
import com.gogomaya.tests.validation.PlayerCredentialsValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = PlayerCredentialsValidation.class)
@ContextConfiguration(classes = { TestConfiguration.class })
public class PlayerOperationsTest {

    @Inject
    PlayerOperations playerOperations;

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

        RegistrationRequest registrationRequest = new RegistrationRequest().setPlayerProfile(profile).setPlayerCredential(playerCredential);

        Player player = playerOperations.createPlayer(registrationRequest);

        assertNotNull(player);
        assertEquals(player.getProfile().getCategory(), PlayerCategory.Amateur);
        assertEquals(player.getProfile().getFirstName(), "Anton");
        assertEquals(player.getProfile().getLastName(), "Oparin");
        assertEquals(player.getProfile().getGender(), PlayerGender.M);
        assertEquals(player.getProfile().getNickName(), "mavarazy");

        try {
            System.out.println(new ObjectMapper().writeValueAsString(registrationRequest));
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void loginExistingUser() {
        PlayerProfile profile = new PlayerProfile().setCategory(PlayerCategory.Amateur).setFirstName("Anton").setLastName("Oparin").setGender(PlayerGender.M)
                .setNickName("mavarazy");

        Player player = playerOperations.createPlayer(profile);

        PlayerCredential loginCredential = new PlayerCredential().setEmail(player.getCredential().getEmail()).setPassword(player.getCredential().getPassword());

        Player loginPlayer = playerOperations.login(loginCredential);

        assertEquals(loginPlayer.getPlayerId(), player.getPlayerId());

        assertEquals(loginPlayer.getCredential().getEmail(), player.getCredential().getEmail());
        assertEquals(loginPlayer.getCredential().getPassword(), player.getCredential().getPassword());

        assertEquals(loginPlayer.getIdentity().getSecret(), player.getIdentity().getSecret());
        assertEquals(loginPlayer.getIdentity().getPlayerId(), player.getIdentity().getPlayerId());

    }
}
