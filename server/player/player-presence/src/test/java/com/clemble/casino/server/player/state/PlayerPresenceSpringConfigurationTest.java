package com.clemble.casino.server.player.state;

import com.clemble.casino.server.spring.web.player.PlayerPresenceSpringConfiguration;
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
@ContextConfiguration(classes = PlayerPresenceSpringConfiguration.class)
public class PlayerPresenceSpringConfigurationTest {

    @Test
    public void test() {
    }

}
