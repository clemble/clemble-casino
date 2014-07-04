package com.clemble.casino.service.spring.player;

import com.clemble.casino.server.spring.player.PlayerProfileSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 7/4/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PlayerProfileSpringConfiguration.class })
public class PlayerProfileSpringConfigurationTest {

    @Test
    public void initialize() {
    }

}
