package com.gogomaya.server.web;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.spring.web.WebMvcSpiSpringConfiguration;
import com.gogomaya.server.tictactoe.TicTacToeState;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { WebMvcSpiSpringConfiguration.class })
public class GogomayaWebInitializationTest {

    @Inject
    GameSessionRepository<TicTacToeState> gameSessionRepository;

    @Test
    public void testInitialized() {
        Assert.assertNotNull(gameSessionRepository);
    }

}
