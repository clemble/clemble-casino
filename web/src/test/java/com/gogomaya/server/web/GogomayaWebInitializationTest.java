package com.gogomaya.server.web;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.tictactoe.TicTacToeState;
import com.gogomaya.server.repository.game.GameSessionRepository;
import com.gogomaya.server.spring.web.WebMvcSpiSpringConfiguration;

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
