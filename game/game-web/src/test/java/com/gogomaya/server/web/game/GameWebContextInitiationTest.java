package com.gogomaya.server.web.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.game.GameState;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.TicTacToeWebSpringConfiguration;
import com.gogomaya.server.web.game.session.GameConstructionController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TicTacToeWebSpringConfiguration.class)
@ActiveProfiles(SpringConfiguration.PROFILE_TEST)
@WebAppConfiguration
public class GameWebContextInitiationTest {

    @Autowired
    private GameConstructionController<GameState> tableMatchController;

    @Test
    public void testInitialized() {
    }

}
