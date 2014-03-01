package com.clemble.casino.server.player;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.clemble.casino.player.PlayerCategory;
import com.clemble.casino.player.PlayerProfile;
import com.clemble.casino.server.repository.player.PlayerProfileRepository;
import com.clemble.casino.server.repository.player.PlayerSocialNetworkRepository;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import com.clemble.casino.server.spring.player.PlayerManagementSpringConfiguration;
import com.clemble.test.random.ObjectGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.TEST)
@ContextConfiguration(classes = PlayerManagementSpringConfiguration.class)
public class PlayerProfileRepositoryTest {

    @Autowired
    public PlayerProfileRepository playerProfileRepository;

    @Autowired
    public PlayerSocialNetworkRepository playerRelationsRepository;

    @Before
    @After
    public void clean() {
        playerProfileRepository.deleteAll();
    }

    @Test
    public void testInitialized() {
        Assert.assertNotNull(playerProfileRepository);
    }

    @Test
    public void testSave() {
        PlayerProfile playerProfile = ObjectGenerator.generate(PlayerProfile.class);
        playerProfile.setImageUrl("http://" + RandomStringUtils.random(10) + ".com/");
        playerProfile.setBirthDate(new Date(0));
        playerProfile.setVersion(0);
        playerProfile.setPlayer(RandomStringUtils.random(5));
        PlayerProfile savedProfile = playerProfileRepository.save(playerProfile);
        assertEquals(savedProfile.getPlayer(), playerProfile.getPlayer());
        PlayerProfile found = playerProfileRepository.findOne(playerProfile.getPlayer());
        Assert.assertEquals(found, playerProfile);
        Assert.assertEquals(found, savedProfile);
    }

    @Test
    public void testSaveMultiple() {
        PlayerProfile playerA = new PlayerProfile().setFirstName("User A").setLastName("Userius")
                .setCategory(PlayerCategory.Novice).setPlayer(RandomStringUtils.random(5));
        PlayerProfile savedPlayerA = playerProfileRepository.save(playerA);
        playerA.setPlayer(savedPlayerA.getPlayer());
        Assert.assertNotNull(playerA.getPlayer());
        Assert.assertNotNull(savedPlayerA.getPlayer());
        PlayerProfile foundPlayerA = playerProfileRepository.findOne(playerA.getPlayer());
        Assert.assertEquals(foundPlayerA, playerA);
        Assert.assertEquals(foundPlayerA, savedPlayerA);

        PlayerProfile playerB = new PlayerProfile().setFirstName("User B").setLastName("Userius")
                .setCategory(PlayerCategory.Amateur).setPlayer(RandomStringUtils.random(5));
        PlayerProfile savedPlayerB = playerProfileRepository.save(playerB);
        playerB.setPlayer(savedPlayerB.getPlayer());
        Assert.assertNotSame(savedPlayerA.getPlayer(), savedPlayerB.getPlayer());
        Assert.assertNotNull(playerB.getPlayer());
        Assert.assertNotNull(savedPlayerB.getPlayer());
        PlayerProfile foundPlayerB = playerProfileRepository.findOne(playerB.getPlayer());
        Assert.assertEquals(foundPlayerB, playerB);
        Assert.assertEquals(foundPlayerB, savedPlayerB);

    }
}
