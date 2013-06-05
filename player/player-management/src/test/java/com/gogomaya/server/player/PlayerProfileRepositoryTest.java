package com.gogomaya.server.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.player.session.PlayerSessionRepository;
import com.gogomaya.server.spring.player.PlayerManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = PlayerManagementSpringConfiguration.class)
public class PlayerProfileRepositoryTest {

    @Autowired
    private PlayerProfileRepository playerProfileRepository;

    @Autowired
    private PlayerSessionRepository sessionRepository;

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
        PlayerProfile gamerProfile = new PlayerProfile();
        PlayerProfile savedProfile = playerProfileRepository.save(gamerProfile);
        gamerProfile.setPlayerId(savedProfile.getPlayerId());
        Assert.assertNotNull(gamerProfile.getPlayerId());
        Assert.assertNotNull(savedProfile.getPlayerId());
        PlayerProfile found = playerProfileRepository.findOne(gamerProfile.getPlayerId());
        Assert.assertEquals(found, gamerProfile);
        Assert.assertEquals(found, savedProfile);
    }

    @Test
    public void testSaveMultiple() {
        PlayerProfile playerA = new PlayerProfile().setFirstName("User A").setLastName("Userius").setCategory(PlayerCategory.Novice);
        PlayerProfile savedPlayerA = playerProfileRepository.saveAndFlush(playerA);
        playerA.setPlayerId(savedPlayerA.getPlayerId());
        Assert.assertNotNull(playerA.getPlayerId());
        Assert.assertNotNull(savedPlayerA.getPlayerId());
        PlayerProfile foundPlayerA = playerProfileRepository.findOne(playerA.getPlayerId());
        Assert.assertEquals(foundPlayerA, playerA);
        Assert.assertEquals(foundPlayerA, savedPlayerA);

        Assert.assertEquals(playerProfileRepository.count(), 1);

        PlayerProfile playerB = new PlayerProfile().setFirstName("User B").setLastName("Userius").setCategory(PlayerCategory.Amateur);
        PlayerProfile savedPlayerB = playerProfileRepository.saveAndFlush(playerB);
        playerB.setPlayerId(savedPlayerB.getPlayerId());
        Assert.assertNotSame(savedPlayerA.getPlayerId(), savedPlayerB.getPlayerId());
        Assert.assertNotNull(playerB.getPlayerId());
        Assert.assertNotNull(savedPlayerB.getPlayerId());
        PlayerProfile foundPlayerB = playerProfileRepository.findOne(playerB.getPlayerId());
        Assert.assertEquals(foundPlayerB, playerB);
        Assert.assertEquals(foundPlayerB, savedPlayerB);

        Assert.assertEquals(playerProfileRepository.count(), 2);

    }
}
