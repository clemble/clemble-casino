package com.gogomaya.server.user;

import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.player.PlayerProfileRepository;
import com.gogomaya.server.player.PlayerProfile;
import com.gogomaya.server.spring.player.PlayerManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = PlayerManagementSpringConfiguration.class)
public class GameProfileRepositoryTest {

    @Autowired
    private PlayerProfileRepository gamerProfileRepository;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void testInitialized(){
        Assert.assertNotNull(gamerProfileRepository);
        Assert.assertNotNull(entityManagerFactory);
    }
    
    @Test
    public void testSave(){
        PlayerProfile gamerProfile = new PlayerProfile();
        PlayerProfile savedProfile = gamerProfileRepository.save(gamerProfile);
        Assert.assertNotNull(gamerProfile.getPlayerId());
        Assert.assertNotNull(savedProfile.getPlayerId());
        PlayerProfile found = gamerProfileRepository.findOne(gamerProfile.getPlayerId());
        Assert.assertEquals(found, gamerProfile);
        Assert.assertEquals(found, savedProfile);
    }
}
