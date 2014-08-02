package com.clemble.casino.server.player;

import com.clemble.casino.server.connection.spring.PlayerConnectionSpringConfiguration;
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
@ContextConfiguration(classes = PlayerConnectionSpringConfiguration.class)
public class PlayerConnectionSpringConfigurationTest {

    @Test
    public void test() {
    }

}
