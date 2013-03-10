package com.gogomaya.server.game.session;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.spring.game.GameManagementConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = GameManagementConfiguration.class)
public class GameSessionRepositoryTest {

    @Autowired
    private GameSessionRepository gameSessionRepository;
    
    @Test
    public void testInitialized() {
        Assert.assertNotNull(gameSessionRepository);
    }
}
