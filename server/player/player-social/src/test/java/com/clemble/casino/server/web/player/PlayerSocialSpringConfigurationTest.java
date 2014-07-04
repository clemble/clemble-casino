package com.clemble.casino.server.web.player;

import com.clemble.casino.server.spring.social.PlayerSocialSpringConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.clemble.casino.server.spring.common.SpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { PlayerSocialSpringConfiguration.class })
public class PlayerSocialSpringConfigurationTest {

    @Test
    public void testInitialized() {
    }

}
