package com.gogomaya.server.web;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.match.TicTacToeStateManager;
import com.gogomaya.server.game.session.TicTacToeSessionRepository;
import com.gogomaya.server.spring.web.WebMvcSpiConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebMvcSpiConfiguration.class })
public class InitializationTest {

    @Inject
    TicTacToeSessionRepository gameSessionRepository;

    @Inject
    TicTacToeStateManager gameMatchingService;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(gameSessionRepository);
        Assert.assertNotNull(gameMatchingService);
    }

}