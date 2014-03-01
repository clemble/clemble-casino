package com.clemble.casino.server.spring.web.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by mavarazy on 01/03/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = GameSpringConfigurationConstructionTest.GameSpringConfiguration.class)
public class GameSpringConfigurationConstructionTest {

    @Configuration
    public static class GameSpringConfiguration extends AbstractGameSpringConfiguration {
    }

    @Test
    public void testInitialized(){
    }

}
