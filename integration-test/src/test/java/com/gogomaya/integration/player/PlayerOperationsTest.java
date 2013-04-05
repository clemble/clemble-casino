package com.gogomaya.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.UUID;

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
import com.gogomaya.server.player.security.PlayerIdentity;
import com.gogomaya.server.player.web.RegistrationRequest;
import com.gogomaya.server.spring.integration.IntegrationTestConfiguration;
import com.gogomaya.tests.validation.PlayerCredentialsValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@UsingSteps(instances = PlayerCredentialsValidation.class)
@ContextConfiguration(classes = { IntegrationTestConfiguration.class })
public class PlayerOperationsTest {

    @Inject
    PlayerOperations playerOperations;

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
}
