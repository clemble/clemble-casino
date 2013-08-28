package com.gogomaya.server.web.player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.spring.common.SpringConfiguration;
import com.gogomaya.server.spring.web.player.PlayerWebSpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles(SpringConfiguration.UNIT_TEST)
@ContextConfiguration(classes = { PlayerWebSpringConfiguration.class })
public class PlayerWebInitializationTest {

    @Test
    public void testInitialized() {
    }

}
