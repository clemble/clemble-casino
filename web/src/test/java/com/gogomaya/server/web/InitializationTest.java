package com.gogomaya.server.web;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.session.GameSessionRepository;
import com.gogomaya.server.spring.web.WebMvcSpiConfiguration;
import com.gogomaya.server.tictactoe.TicTacToeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebMvcSpiConfiguration.class })
public class InitializationTest {

    @Inject
    GameSessionRepository<TicTacToeState> gameSessionRepository;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(gameSessionRepository);
    }

}
