package com.clemble.casino.server.game.construction;

import com.clemble.casino.server.game.construction.spring.GameConstructionSpringConfiguration;
import com.clemble.casino.server.spring.common.SpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by mavarazy on 8/31/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = GameConstructionSpringConfiguration.class)
public class GameConstructionSpringConfigurationTest {

    @Test
    public void testInitialized(){
    }

}
