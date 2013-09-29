package com.gogomaya.server.player;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.player.PlayerCategory;
import com.gogomaya.player.PlayerProfile;
import com.gogomaya.server.repository.player.PlayerProfileRepository;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.player.PlayerManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = PlayerManagementSpringConfiguration.class)
public class PlayerProfileRepositoryTest {

    @Autowired
    public PlayerProfileRepository playerProfileRepository;

    @Before
    public void clean() {
        playerProfileRepository.deleteAll();
    }

    @Test
    public void testInitialized() {
        Assert.assertNotNull(playerProfileRepository);
    }

    @Test
    public void testSave() {
        PlayerProfile gamerProfile = new PlayerProfile().setPlayer(RandomStringUtils.random(5));
        PlayerProfile savedProfile = playerProfileRepository.save(gamerProfile);
        gamerProfile.setPlayer(savedProfile.getPlayer());
        Assert.assertNotNull(gamerProfile.getPlayer());
        Assert.assertNotNull(savedProfile.getPlayer());
        PlayerProfile found = playerProfileRepository.findOne(gamerProfile.getPlayer());
        Assert.assertEquals(found, gamerProfile);
        Assert.assertEquals(found, savedProfile);
    }

    @Test
    public void testSaveMultiple() {
        PlayerProfile playerA = new PlayerProfile().setPlayer(RandomStringUtils.random(5)).setFirstName("User A").setLastName("Userius").setCategory(PlayerCategory.Novice);
        PlayerProfile savedPlayerA = playerProfileRepository.save(playerA);
        playerA.setPlayer(savedPlayerA.getPlayer());
        Assert.assertNotNull(playerA.getPlayer());
        Assert.assertNotNull(savedPlayerA.getPlayer());
        PlayerProfile foundPlayerA = playerProfileRepository.findOne(playerA.getPlayer());
        Assert.assertEquals(foundPlayerA, playerA);
        Assert.assertEquals(foundPlayerA, savedPlayerA);

        Assert.assertEquals(playerProfileRepository.count(), 1);

        PlayerProfile playerB = new PlayerProfile().setPlayer(RandomStringUtils.random(5)).setFirstName("User B").setLastName("Userius").setCategory(PlayerCategory.Amateur);
        PlayerProfile savedPlayerB = playerProfileRepository.save(playerB);
        playerB.setPlayer(savedPlayerB.getPlayer());
        Assert.assertNotSame(savedPlayerA.getPlayer(), savedPlayerB.getPlayer());
        Assert.assertNotNull(playerB.getPlayer());
        Assert.assertNotNull(savedPlayerB.getPlayer());
        PlayerProfile foundPlayerB = playerProfileRepository.findOne(playerB.getPlayer());
        Assert.assertEquals(foundPlayerB, playerB);
        Assert.assertEquals(foundPlayerB, savedPlayerB);

        Assert.assertEquals(playerProfileRepository.count(), 2);

    }
}
