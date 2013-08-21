package com.gogomaya.server.game.tictactoe;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.tictactoe.PicPacPoeSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(SpringConfiguration.PROFILE_TEST)
@ContextConfiguration(classes = { PicPacPoeSpringConfiguration.class })
public class InitializationTest {

    @Test
    public void testInitialized() {
    }

}
