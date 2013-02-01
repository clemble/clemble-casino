package com.gogomaya.server.user;

import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.spring.user.UserModuleSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = UserModuleSpringConfiguration.class)
public class GameProfileRepositoryTest {

    @Autowired
    private GamerProfileRepository gamerProfileRepository;
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    
    @Test
    public void testInitialized(){
        Assert.assertNotNull(gamerProfileRepository);
        Assert.assertNotNull(entityManagerFactory);
    }
    
    @Test
    public void testSave(){
        GamerProfile gamerProfile = new GamerProfile();
        GamerProfile savedProfile = gamerProfileRepository.save(gamerProfile);
        Assert.assertNotNull(gamerProfile.getUserId());
        Assert.assertNotNull(savedProfile.getUserId());
        GamerProfile found = gamerProfileRepository.findOne(gamerProfile.getUserId());
        Assert.assertEquals(found, gamerProfile);
        Assert.assertEquals(found, savedProfile);
    }
}
