package com.clemble.casino.server.game.configuration;

import com.clemble.casino.server.game.configuration.controller.GameConfigurationController;
import com.clemble.casino.server.game.configuration.spring.GameConfigurationSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 8/30/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {GameConfigurationSpringConfiguration.class})
public class GameConfigurationSpringConfigurationTest {

    @Autowired
    public GameConfigurationController controller;

    @Test
    public void testMe(){
        Assert.assertNotNull(controller);
    }

}
