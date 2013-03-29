package com.gogomaya.server.game.session;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.game.table.GameTableManager;
import com.gogomaya.server.spring.game.GameManagementSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = { GameManagementSpringConfiguration.class })
public class GameSessionRepositoryTest {

    @Inject
    private GameSessionRepository sessionRepository;

    @Inject
    private GameTableManager tableManager;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(sessionRepository);
        Assert.assertNotNull(tableManager);
    }
}
