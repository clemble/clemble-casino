package com.gogomaya.server.web.player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.gogomaya.server.spring.web.player.WebPlayerConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(classes = { WebPlayerConfiguration.class })
public class InitializationTest {

    @Test
    public void testInitialized() {
    }

}
