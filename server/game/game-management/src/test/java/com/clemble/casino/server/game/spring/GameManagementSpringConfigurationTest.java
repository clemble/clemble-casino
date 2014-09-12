package com.clemble.casino.server.game.spring;

import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 9/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {GameManagementSpringConfiguration.class})
public class GameManagementSpringConfigurationTest {

    @Test
    public void testInitializes(){
    }

}
