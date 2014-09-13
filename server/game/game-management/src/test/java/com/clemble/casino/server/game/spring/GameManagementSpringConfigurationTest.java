package com.clemble.casino.server.game.spring;

import com.clemble.casino.server.game.listener.ServerGameStartedEventListener;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 9/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {GameManagementSpringConfiguration.class})
public class GameManagementSpringConfigurationTest {

    @Autowired
    public ServerGameStartedEventListener eventListener;

    @Test
    public void testInitializes(){
    }

}
