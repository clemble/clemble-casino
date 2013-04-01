package com.gogomaya.integration.player;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

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
        PlayerProfile profile = new PlayerProfile()
            .setCategory(PlayerCategory.Amateur)
            .setFirstName("Anton")
            .setLastName("Oparin")
            .setGender(PlayerGender.M)
            .setNickName("mavarazy");

        Player player = playerOperations.createPlayer(profile);

        assertNotNull(player);
        assertEquals(player.getProfile().getCategory(), PlayerCategory.Amateur);
        assertEquals(player.getProfile().getFirstName(), "Anton");
        assertEquals(player.getProfile().getLastName(), "Oparin");
        assertEquals(player.getProfile().getGender(), PlayerGender.M);
        assertEquals(player.getProfile().getNickName(), "mavarazy");
    }

}
